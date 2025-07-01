package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.exceptions.user.UserAlreadyExistsException;
import ru.akvine.iskra.exceptions.user.UserNotFoundException;
import ru.akvine.iskra.repositories.UserRepository;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.user.UserModel;
import ru.akvine.iskra.services.domain.user.dto.CreateUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserModel create(CreateUser action) {
        Asserts.isNotNull(action);

        String email = action.getEmail();
        String username = action.getUsername();
        if (existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email = [" + email + "] already exists!");
        }

        if (existsByUsername(username)) {
            throw new UserAlreadyExistsException("User with username = [" + email + "] already exists!");
        }

        String hash = passwordEncoder.encode(action.getPassword());
        UserEntity user = new UserEntity()
                .setUsername(action.getUsername())
                .setEmail(email)
                .setPassword(hash)
                .setLanguage(action.getLanguage());
        user.setUuid(UUIDGenerator.uuidWithoutDashes());
        return new UserModel(userRepository.save(user));
    }

    @Override
    public UserEntity verifyExistsByUuid(String uuid) {
        Asserts.isNotBlank(uuid, "userUuid is null");
        return userRepository
                .findByUuid(uuid)
                .orElseThrow(() -> {
                    String message = String.format("User with uuid = [" + uuid + "] is not found!");
                    return new UserNotFoundException(message);
                });
    }

    @Override
    public UserEntity verifyExistsByUsername(String username) {
        Asserts.isNotNull(username);
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    String message = String.format("User with username = [" + username + "] is not found!");
                    return new UserNotFoundException(message);
                });
    }

    @Override
    public UserEntity verifyExistsByEmail(String email) {
        Asserts.isNotNull(email);
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> {
                    String message = String.format("User with email = [" + email + "] is not found!");
                    return new UserNotFoundException(message);
                });
    }

    @Override
    public boolean existsByEmail(String email) {
        Asserts.isNotBlank(email, "email is null");

        try {
            verifyExistsByEmail(email);
            return true;
        } catch (UserNotFoundException exception) {
            return false;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Asserts.isNotBlank(username, "username is null");

        try {
            verifyExistsByUsername(username);
            return true;
        } catch (UserNotFoundException exception) {
            return false;
        }
    }
}
