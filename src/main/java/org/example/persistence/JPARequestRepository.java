package org.example.persistence;

import org.example.persistence.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JPARequestRepository  extends JpaRepository<RequestEntity, Long> {
    @Query("select r from RequestEntity r where r.userId = :userId and r.exerciseId = :exerciseId")
    List<RequestEntity> getUserExercises(@Param("userId") long userId, @Param("exerciseId") long exerciseId);



}
