package org.example.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExercise {
    private Long id;
    private User user;
    private Exercise exercise;
    private LocalDate LocalDate;
}
