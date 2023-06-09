package org.example.buisness;

import org.example.domain.ExerciseCount;
import org.example.domain.ExerciseIntensity;
import java.time.LocalDate;
import java.util.List;

public interface StatisticsManager {
    List<ExerciseCount> countUsersPerExerciseInInterval(LocalDate startDate, LocalDate endDate);
     List<ExerciseIntensity> calculateAverageExerciseIntensity(LocalDate startDate, LocalDate endDate);

}
