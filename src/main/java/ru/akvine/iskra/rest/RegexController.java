package ru.akvine.iskra.rest;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.regex.CreateRegexRequest;
import ru.akvine.iskra.rest.mappers.RegexMapper;
import ru.akvine.iskra.rest.meta.RegexControllerMeta;
import ru.akvine.iskra.services.domain.regex.RegexModel;
import ru.akvine.iskra.services.domain.regex.RegexService;
import ru.akvine.iskra.services.domain.regex.dto.CreateRegex;

@RestController
@RequiredArgsConstructor
public class RegexController implements RegexControllerMeta {
    private final RegexMapper regexMapper;
    private final RegexService regexService;
    private final SecurityManager securityManager;

    @Override
    public Response list() {
        List<RegexModel> regexps =
                regexService.list(securityManager.getCurrentUser().getUuid());
        return regexMapper.mapToListRegexResponse(regexps);
    }

    @Override
    public Response create(CreateRegexRequest request) {
        CreateRegex action = regexMapper.mapToCreateRegexRequest(request);
        RegexModel createdRegex = regexService.create(action);
        return regexMapper.mapToListRegexResponse(List.of(createdRegex));
    }
}
