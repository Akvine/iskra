package ru.akvine.iskra.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.akvine.compozit.commons.dto.ErrorResponse;
import ru.akvine.iskra.constants.ApiErrorCodes;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ErrorResponse AUTH_FAIL_RESPONSE =
            new ErrorResponse(ApiErrorCodes.NO_SESSION, "User authentication is required");

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().println(objectMapper.writeValueAsString(AUTH_FAIL_RESPONSE));
    }
}
