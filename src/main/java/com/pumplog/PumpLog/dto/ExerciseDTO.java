package com.pumplog.PumpLog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExerciseDTO {

    private Long id;
    private String name;
    private String sets;
    private String breakDuration;
    private String workoutPlanName;
}
