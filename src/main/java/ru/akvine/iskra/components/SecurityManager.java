package ru.akvine.iskra.components;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.configs.security.UserAuthentication;
import ru.akvine.iskra.exceptions.NoSessionException;
import ru.akvine.iskra.services.domain.user.UserModel;

@Component
@RequiredArgsConstructor
public class SecurityManager {

    public void authenticate(UserModel userModel, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UserAuthentication(
                userModel.getId(), userModel.getUuid(), userModel.getUsername(), userModel.getEmail()));

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);
    }

    @Nullable
    public UserAuthentication getCurrentUserOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return (UserAuthentication) authentication;
        }
        return null;
    }

    public void doLogout(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        UserAuthentication user = getCurrentUserOrNull();
        if (user == null) {
            return;
        }

        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        SecurityContextHolder.clearContext();
    }

    public UserAuthentication getCurrentUser() {
        UserAuthentication user = getCurrentUserOrNull();
        Asserts.isNotNull(user, "user is null");
        return user;
    }

    public HttpSession getSession(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new NoSessionException();
        }
        return session;
    }
}
