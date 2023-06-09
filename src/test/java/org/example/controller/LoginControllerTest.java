package org.example.controller;

import com.github.dockerjava.api.exception.UnauthorizedException;
import org.example.buisness.LoginManager;
import org.example.buisness.exceptions.InvalidLoginRequestException;
import org.example.controller.dto.LoginRequest;
import org.example.controller.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Test
    @DisplayName("Should return a bad request response when the login request is invalid")
    void loginWhenLoginRequestIsInvalid() {
        LoginManager loginManager = mock(LoginManager.class);
        LoginController loginController = new LoginController(loginManager);
        LoginRequest loginRequest = new LoginRequest("", "");

        when(loginManager.login(loginRequest)).thenThrow(new InvalidLoginRequestException());

        Exception exception = assertThrows(InvalidLoginRequestException.class, () -> loginController.login(loginRequest));

        assertTrue(exception instanceof InvalidLoginRequestException);
        verify(loginManager, times(1)).login(loginRequest);
    }

    @Test
    @DisplayName("Should return an bad request response when the credentials are incorrect")
    void loginWhenCredentialsAreIncorrect() {
        LoginManager loginManager = mock(LoginManager.class);
        LoginController loginController = new LoginController(loginManager);
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");

        ResponseEntity<LoginResponse> responseEntity = loginController.login(loginRequest);

        assertNull(responseEntity.getBody());
        assertEquals(400, responseEntity.getStatusCodeValue());
        verify(loginManager, times(1)).login(loginRequest);
    }


    @Test
    @DisplayName("Should return a valid login response when the credentials are correct")
    void loginWhenCredentialsAreCorrect() {
        LoginManager loginManager = mock(LoginManager.class);
        LoginController loginController = new LoginController(loginManager);
        LoginRequest loginRequest =
                LoginRequest.builder().email("test@example.com").password("password").build();
        LoginResponse expectedLoginResponse =
                LoginResponse.builder()
                        .accessToken("access_token")
                        .roles(List.of("ROLE_USER"))
                        .userId(1L)
                        .build();
        when(loginManager.login(loginRequest)).thenReturn(expectedLoginResponse);

        ResponseEntity<LoginResponse> responseEntity = loginController.login(loginRequest);

        assertEquals(expectedLoginResponse, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getHeaders().get("Authorization"));
        verify(loginManager, times(1)).login(loginRequest);
    }
}

