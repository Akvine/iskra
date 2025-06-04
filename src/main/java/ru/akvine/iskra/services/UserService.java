package ru.akvine.iskra.services;

import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.domain.user.UserModel;
import ru.akvine.iskra.services.dto.user.CreateUser;

public interface UserService {
    UserModel create(CreateUser action);

    UserEntity verifyExistsByUuid(String uuid);

    UserEntity verifyExistsByUsername(String username);

    UserEntity verifyExistsByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
