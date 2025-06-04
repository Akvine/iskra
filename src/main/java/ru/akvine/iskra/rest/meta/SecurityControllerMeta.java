package ru.akvine.iskra.rest.meta;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.security.AuthRequest;
import ru.akvine.iskra.rest.dto.security.RegistrationRequest;

@RequestMapping(value = "/security")
public interface SecurityControllerMeta {
    @PostMapping(value = "/registration")
    Response registration(@RequestBody @Valid RegistrationRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/auth")
    Response auth(@RequestBody @Valid AuthRequest request, HttpServletRequest httpServletRequest);

    @PostMapping(value = "/logout")
    Response logout(HttpServletRequest request);
}
