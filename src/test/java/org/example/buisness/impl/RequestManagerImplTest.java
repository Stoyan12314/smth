package org.example.buisness.impl;

import org.example.buisness.RequestManager;
import org.example.buisness.exceptions.EmptyListExercises;
import org.example.buisness.exceptions.InvalidRequestException;
import org.example.controller.dto.CreateRequestRequest;
import org.example.controller.dto.RegisterRequest;
import org.example.controller.dto.RequestResponse;
import org.example.domain.AccessToken;
import org.example.domain.Request;
import org.example.persistence.RequestRepository;
import org.example.persistence.entity.RequestEntity;
import org.example.security.auth.RequestAuthenticatedUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestManagerImplTest {

    @Test
    @DisplayName("Should throw an InvalidRequestException when the authenticated user is different from the user")
    void assignExerciseWhenUserIsDifferentFromAuthenticatedUserThenThrowInvalidRequestException() {
        RequestRepository requestRepository = mock(RequestRepository.class);
        RequestAuthenticatedUserProvider requestAuthenticatedUserProvider = mock(RequestAuthenticatedUserProvider.class);

        RequestManagerImpl requestManager = new RequestManagerImpl(requestRepository, requestAuthenticatedUserProvider);

        CreateRequestRequest createRequestRequest = new CreateRequestRequest();
        createRequestRequest.setUserId(1L);

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(new AccessToken("subject", Collections.emptyList(), 2L));

        assertThrows(
                InvalidRequestException.class,
                () -> requestManager.assignExercise(createRequestRequest),
                "Should throw an InvalidRequestException when the authenticated user is different from the user");
    }

    @Test
    @DisplayName("Should return RequestResponse when the authenticated user is the user")
    void assignExerciseWhenUserIsAuthenticated() {
        RequestRepository requestRepository = mock(RequestRepository.class);
        RequestAuthenticatedUserProvider requestAuthenticatedUserProvider = mock(RequestAuthenticatedUserProvider.class);
        RequestManagerImpl requestManager = new RequestManagerImpl(requestRepository, requestAuthenticatedUserProvider);

        long userId = 1L;
        CreateRequestRequest createRequestRequest = new CreateRequestRequest();
        createRequestRequest.setUserId(userId);

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setId(1L);

        when(requestAuthenticatedUserProvider.getAuthenticatedUserInRequest()).thenReturn(new AccessToken("subject", Collections.emptyList(), userId));
        when(requestRepository.assignExercise(any(RequestEntity.class))).thenReturn(requestEntity);

        RequestResponse result = requestManager.assignExercise(createRequestRequest);

        assertNotNull(result);
        assertEquals(requestEntity.getId(), result.getId());
        verify(requestRepository, times(1)).assignExercise(any(RequestEntity.class));
        verify(requestAuthenticatedUserProvider, times(1)).getAuthenticatedUserInRequest();
    }






}