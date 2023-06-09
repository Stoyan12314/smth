package org.example.persistence;

import org.example.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository {
    Page<UserEntity> getAllUsers(Pageable pageable);
    Page<UserEntity> getUsersByEmail(String email, Pageable pageable);
    UserEntity findUserByEmail(String email);
    Optional<UserEntity> findUserById(Long id);
    UserEntity saveUser(UserEntity user);
}
