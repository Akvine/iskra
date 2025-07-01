package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.regex.CreateRegexRequest;
import ru.akvine.iskra.rest.dto.regex.ListRegexResponse;
import ru.akvine.iskra.rest.dto.regex.RegexDto;
import ru.akvine.iskra.services.domain.regex.RegexModel;
import ru.akvine.iskra.services.domain.regex.dto.CreateRegex;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegexMapper {
    private final SecurityManager securityManager;

    public CreateRegex mapToCreateRegexRequest(CreateRegexRequest request) {
        Asserts.isNotNull(request);
        return new CreateRegex()
                .setName(request.getName())
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setPattern(request.getPattern())
                .setDescription(request.getDescription());
    }

    public ListRegexResponse mapToListRegexResponse(List<RegexModel> regexps) {
        Asserts.isNotNull(regexps);
        return new ListRegexResponse().setRegexps(regexps.stream().map(this::buildRegexDto).toList());
    }

    private RegexDto buildRegexDto(RegexModel model) {
        return new RegexDto()
                .setName(model.getName())
                .setPattern(model.getPattern())
                .setDescription(model.getDescription())
                .setSystem(model.isSystem());
    }
}
