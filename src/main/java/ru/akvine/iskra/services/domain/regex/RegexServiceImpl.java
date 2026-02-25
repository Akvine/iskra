package ru.akvine.iskra.services.domain.regex;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.exceptions.regex.RegexAlreadyExistsException;
import ru.akvine.iskra.exceptions.regex.RegexNotFoundException;
import ru.akvine.iskra.repositories.RegexRepository;
import ru.akvine.iskra.repositories.entities.RegexEntity;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.regex.dto.CreateRegex;

@Service
@RequiredArgsConstructor
public class RegexServiceImpl implements RegexService {
    private final RegexRepository regexRepository;
    private final UserService userService;

    @Override
    public List<RegexModel> list(String userUuid) {
        Asserts.isNotBlank(userUuid);
        return regexRepository.find(userUuid).stream().map(RegexModel::new).toList();
    }

    @Override
    public RegexModel create(CreateRegex action) {
        Asserts.isNotNull(action);

        String name = action.getName();
        String userUuid = action.getUserUuid();
        UserEntity user = userService.verifyExistsByUuid(userUuid);

        try {
            verifyExistsByName(name, userUuid);
            String errorMessage = String.format("Regex with name = [%s] already exists!", name);
            throw new RegexAlreadyExistsException(errorMessage);
        } catch (RegexNotFoundException exception) {
            RegexEntity regexToCreate = new RegexEntity()
                    .setUuid(UUIDGenerator.uuid())
                    .setName(name)
                    .setPattern(action.getPattern())
                    .setDescription(action.getDescription())
                    .setUser(user);
            return new RegexModel(regexRepository.save(regexToCreate));
        }
    }

    @Override
    public RegexEntity verifyExistsByName(String name, String userUuid) {
        Asserts.isNotBlank(name);
        Asserts.isNotBlank(userUuid);
        return regexRepository.find(userUuid, name).orElseThrow(() -> {
            String errorMessage =
                    String.format("Regex with name = [%s] for user with uuid = [%s] is not found", name, userUuid);
            return new RegexNotFoundException(errorMessage);
        });
    }
}
