package ru.akvine.iskra.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.security.AuthRequest;
import ru.akvine.iskra.rest.dto.security.RegistrationRequest;
import ru.akvine.iskra.rest.mappers.UserMapper;
import ru.akvine.iskra.rest.meta.SecurityControllerMeta;
import ru.akvine.iskra.rest.validators.CredentialsValidator;
import ru.akvine.iskra.rest.validators.SecurityValidator;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.user.UserModel;
import ru.akvine.iskra.services.domain.user.dto.CreateUser;

@RestController
@RequiredArgsConstructor
public class SecurityController implements SecurityControllerMeta {
    private final UserService userService;
    private final SecurityManager securityManager;
    private final CredentialsValidator credentialsValidator;
    private final SecurityValidator securityValidator;
    private final UserMapper userMapper;

    @Override
    public Response registration(RegistrationRequest request, HttpServletRequest httpServletRequest) {
        securityValidator.verifyRegistrationRequest(request);
        CreateUser action = userMapper.mapToCreateUser(request);
        UserModel user = userService.create(action);
        securityManager.authenticate(user, httpServletRequest);
        return userMapper.mapToAuthResponse(user);
    }

    @Override
    public Response auth(AuthRequest request, HttpServletRequest httpServletRequest) {
        UserModel user =
                credentialsValidator.validateCredentialsByUsername(request.getUsername(), request.getPassword());
        securityManager.authenticate(user, httpServletRequest);
        return userMapper.mapToAuthResponse(user);
    }

    @Override
    public Response logout(HttpServletRequest request) {
        securityManager.doLogout(request);
        return new SuccessfulResponse();
    }
}
