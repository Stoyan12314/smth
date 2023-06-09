
package org.example.buisness.impl;

import org.example.buisness.AccessTokenEncoder;
import org.example.buisness.LoginManager;
import org.example.buisness.exceptions.InvalidCredentialsException;
import org.example.buisness.exceptions.InvalidLoginRequestException;
import org.example.controller.converters.UserConverter;
import org.example.controller.dto.LoginRequest;
import org.example.controller.dto.LoginResponse;
import org.example.domain.AccessToken;
import org.example.domain.User;
import org.example.persistence.UserRepository;
import org.example.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.domain.Role;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginManagerImplTest {

    @Test
    @DisplayName("Should throw a InvalidLoginRequestException when loginRequest is null")
    void loginWhenLoginRequestIsNullThenThrowInvalidLoginRequestException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);
        LoginManagerImpl loginManager =
                new LoginManagerImpl(userRepository, passwordEncoder, accessTokenEncoder);

        assertThrows(
                InvalidLoginRequestException.class,
                () -> loginManager.login(null));
    }

    @Test
    @DisplayName("Should throw a InvalidCredentialsException when user is not found")
    void loginWhenUserIsNotFoundThenThrowInvalidCredentialsException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);
        LoginManagerImpl loginManager =
                new LoginManagerImpl(userRepository, passwordEncoder, accessTokenEncoder);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        when(userRepository.findUserByEmail(loginRequest.getEmail())).thenReturn(null);



        assertThrows(
                InvalidCredentialsException.class,
                () -> loginManager.login(loginRequest));


    }

    @Test
    @DisplayName("Should throw a InvalidCredentialsException when password does not match")
    void loginWhenPasswordDoesNotMatchThenThrowInvalidCredentialsException() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);
        LoginManagerImpl loginManager =
                new LoginManagerImpl(userRepository, passwordEncoder, accessTokenEncoder);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encryptedPassword");

        when(userRepository.findUserByEmail(loginRequest.getEmail())).thenReturn(userEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> loginManager.login(loginRequest));
    }

    @Test    @DisplayName("Should return a LoginResponse when login is successful")
    void loginWhenLoginIsSuccessful() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        AccessTokenEncoder accessTokenEncoder = mock(AccessTokenEncoder.class);
        LoginManagerImpl loginManager =
                new LoginManagerImpl(userRepository, passwordEncoder, accessTokenEncoder);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encryptedPassword");
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setRole(Role.CUSTOMER);

        User user = UserConverter.convert(userEntity);

        AccessToken accessToken = AccessToken.builder()
                .subject(user.getEmail())
                .roles(Arrays.asList(user.getRole().toString()))
                .userId(user.getId())
                .build();

        when(userRepository.findUserByEmail(loginRequest.getEmail())).thenReturn(userEntity);
        when(passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(accessToken)).thenReturn("encodedAccessToken");

        LoginResponse result = loginManager.login(loginRequest);

        assertNotNull(result);
        assertEquals("encodedAccessToken", result.getAccessToken());
        assertEquals(userEntity.getId(), result.getUserId());
        assertEquals(Arrays.asList(userEntity.getRole().toString()), result.getRoles());
    }

}
