package com.pumplog.PumpLog.mapper;

import com.pumplog.PumpLog.dto.ExerciseDTO;
import com.pumplog.PumpLog.model.Exercise;
import com.pumplog.PumpLog.model.WorkoutPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    @Mapping(source = "workoutPlanList", target = "workoutPlanName", qualifiedByName = "workoutPlanListToName") // dato che voglio stampare solo il nome del WorkoutPlan all'interno di exerciseDTO
    public ExerciseDTO toDto(Exercise exercise);

    @Mapping(source = "workoutPlanName", target = "workoutPlanList", ignore = true) // aggiungo gli exercise al workoutPlan (e viceversa) nel service
    public Exercise toEntity(ExerciseDTO exerciseDTO);

    public List<ExerciseDTO> toDtoList(List<Exercise> exerciseList);

    public List<Exercise> toEntityList(List<ExerciseDTO> exerciseDTOList);

    @Named("workoutPlanListToName")
    static String workoutPlanListToName(List<WorkoutPlan> workoutPlanList) {
        return workoutPlanList != null ? workoutPlanList.stream()
                .map(WorkoutPlan::getName)
                .collect(Collectors.joining(", ")) : null;
    }
}
