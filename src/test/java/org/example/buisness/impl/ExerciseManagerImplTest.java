package org.example.buisness.impl;

import org.example.buisness.exceptions.CreateExerciseException;
import org.example.buisness.exceptions.NotFoundExerciseException;
import org.example.buisness.impl.ExerciseManagerImpl;
import org.example.controller.dto.UpdateExerciseRequest;
import org.example.controller.converters.ExerciseConverter;
import org.example.domain.Exercise;
import org.example.persistence.ExerciseRepo;
import org.example.persistence.entity.ExerciseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseManagerImplTest {

    @Test
    @DisplayName("Should throw a NotFoundExerciseException when the exercise is not found")
    void getExerciseWhenExerciseIsNotFoundThenThrowNotFoundExerciseException() {
        ExerciseRepo exerciseRepo = mock(ExerciseRepo.class);
        ExerciseManagerImpl exerciseManager =
                new ExerciseManagerImpl(exerciseRepo);

        long id = 1L;

        when(exerciseRepo.findById(id)).thenReturn(null);

        assertThrows(
                NotFoundExerciseException.class,
                () -> exerciseManager.getExercise(id),
                "Should throw a NotFoundExerciseException when the exercise is not found");
    }

    @Test
    @DisplayName("Should return an Exercise when the exercise is found")
    void getExerciseWhenExerciseIsFound() {
        ExerciseRepo exerciseRepo = mock(ExerciseRepo.class);
        ExerciseManagerImpl exerciseManager =
                new ExerciseManagerImpl(exerciseRepo);

        long id = 1L;
        ExerciseEntity exerciseEntity = new ExerciseEntity();
        Exercise exercise = ExerciseConverter.convertExercise(exerciseEntity);

        when(exerciseRepo.findById(id)).thenReturn(exerciseEntity);

        Exercise result = exerciseManager.getExercise(id);

        assertNotNull(result);
        assertEquals(exercise.getId(), result.getId());
    }

    @Test
    @DisplayName("Should throw a CreateExerciseException when the exercise creation failed")
    void createExerciseWhenExerciseCreationFailedThenThrowCreateExerciseException() {
        ExerciseRepo exerciseRepo = mock(ExerciseRepo.class);
        ExerciseManagerImpl exerciseManager =
                new ExerciseManagerImpl(exerciseRepo);

        Exercise exercise = new Exercise();

        when(exerciseRepo.createExercise(exercise)).thenReturn(null);

        assertThrows(
                CreateExerciseException.class,
                () -> exerciseManager.createExercise(exercise),
                "Should throw a CreateExerciseException when the exercise creation failed");
    }

    @Test
    @DisplayName("Should return the exercise ID when the exercise creation is successful")
    void createExerciseWhenExerciseCreationIsSuccessful() {
        ExerciseRepo exerciseRepo = mock(ExerciseRepo.class);
        ExerciseManagerImpl exerciseManager =
                new ExerciseManagerImpl(exerciseRepo);

        Exercise exercise = new Exercise();
        Long id = 1L;

        when(exerciseRepo.createExercise(exercise)).thenReturn(id);

        Long result = exerciseManager.createExercise(exercise);

        assertNotNull(result);
        assertEquals(id, result);
    }
}
