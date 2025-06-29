package ru.akvine.iskra.services.domain.plan;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.components.NameGenerator;
import ru.akvine.iskra.events.LoadMetadataEvent;
import ru.akvine.iskra.exceptions.plan.PlanNotFoundException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.connection.ConnectionService;
import ru.akvine.iskra.services.dto.plan.CreatePlan;
import ru.akvine.iskra.services.dto.plan.DuplicatePlan;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final ConnectionService connectionService;
    private final UserService userService;

    private final NameGenerator nameGenerator;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public PlanModel create(CreatePlan createPlan) {
        Asserts.isNotNull(createPlan);
        ConnectionEntity connection = connectionService.verifyExists(createPlan.getConnectionName(), createPlan.getUserUuid());

        UserEntity owner = userService.verifyExistsByUuid(createPlan.getUserUuid());
        PlanEntity plan = new PlanEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes())
                .setName(createPlan.getName())
                .setConnection(connection)
                .setUser(owner);
        return new PlanModel(planRepository.save(plan));
    }

    @Override
    public PlanModel duplicate(DuplicatePlan duplicatePlan) {
        Asserts.isNotNull(duplicatePlan);

        String planUuid = duplicatePlan.getUuid();
        String userUuid = duplicatePlan.getUserUuid();
        UserEntity user = userService.verifyExistsByUuid(userUuid);

        PlanEntity from = verifyExists(planUuid, userUuid);
        PlanEntity target = new PlanEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes())
                .setUser(user)
                .setConnection(from.getConnection())
                .setGenerateScriptsForNotNull(from.isGenerateScriptsForNotNull())
                .setGenerateScriptsForIndex(from.isGenerateScriptsForIndex())
                .setGenerateScriptsForPrimaryKey(from.isGenerateScriptsForPrimaryKey())
                .setGenerateScriptsForTrigger(from.isGenerateScriptsForTrigger())
                .setGenerateScriptsForUnique(from.isGenerateScriptsForUnique())
                .setGenerateScriptsForCheck(from.isGenerateScriptsForCheck())
                .setGenerateScriptsForDefault(from.isGenerateScriptsForDefault());

        if (StringUtils.isNotBlank(duplicatePlan.getName()) &&
                !duplicatePlan.getName().equals(from.getName())) {
            target.setName(duplicatePlan.getName());
        } else {
            List<String> generatedNames = nameGenerator.tryGetIncrementedNames(from.getName(), 1);
            if (CollectionUtils.isEmpty(generatedNames)) {
                target.setName(from.getName() + "_1");
            } else {
                target.setName(generatedNames.getFirst());
            }
        }

        if (duplicatePlan.isCopyResults()) {
            target.setRelationsMatrix(from.getRelationsMatrix());
            PlanModel savedPlan = new PlanModel(planRepository.save(target));
            // TODO: сделано для того, чтобы избежать циклической зависимости между PlanService <-> MetadataLoaderService
            publisher.publishEvent(new LoadMetadataEvent(this, savedPlan.getUuid(), userUuid));
            return savedPlan;
        }

        return new PlanModel(planRepository.save(target));
    }

    @Override
    public PlanModel get(String uuid, String userUuid) {
        return new PlanModel(verifyExists(uuid, userUuid));
    }

    @Override
    @Transactional
    public List<PlanModel> list(String uuid) {
        Asserts.isNotNull(uuid);
        return planRepository
                .findAll(uuid).stream()
                .map(PlanModel::new)
                .toList();
    }

    @Override
    public PlanEntity verifyExists(String byUuid, String userUuid) {
        Asserts.isNotNull(byUuid);
        return planRepository
                .findByUuid(byUuid, userUuid)
                .orElseThrow(() -> new PlanNotFoundException("Plan with uuid = [" + byUuid + "] is not found!"));
    }

    @Override
    public PlanModel update(UpdatePlan action) {
        Asserts.isNotNull(action);

        PlanEntity planToUpdate = verifyExists(action.getPlanUuid(), action.getUserUuid());

        if (StringUtils.isNotBlank(action.getLastProcessUuid()) && (
                StringUtils.isBlank(planToUpdate.getLastProcessUuid()) || !action.getLastProcessUuid().equals(planToUpdate.getLastProcessUuid()))) {
            planToUpdate.setLastProcessUuid(action.getLastProcessUuid());
        }

        if (action.getRelationsMatrix() != null &&
                !action.getRelationsMatrix().equals(planToUpdate.getRelationsMatrix())) {
            planToUpdate.setRelationsMatrix(action.getRelationsMatrix());
        }

        return new PlanModel(planRepository.save(planToUpdate));
    }
}
