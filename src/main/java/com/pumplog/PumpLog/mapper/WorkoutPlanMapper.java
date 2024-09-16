package com.pumplog.PumpLog.mapper;

import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.model.Exercise;
import com.pumplog.PumpLog.model.WorkoutPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WorkoutPlanMapper {

    @Mapping(source = "user.username", target = "user")
    @Mapping(source = "exerciseList", target = "exerciseNames", qualifiedByName = "exerciseListToNames")  // dato che voglio stampare solo il nome degli Exercise all'interno di WorkoutPlanDTO
    public WorkoutPlanDTO toDto(WorkoutPlan workoutPlan);

    @Mapping(source = "user", target = "user.username")
    public WorkoutPlan toEntity(WorkoutPlanDTO workoutPlanDTO);

    public List<WorkoutPlanDTO> toDtoList(List<WorkoutPlan> workoutPlanList);

    public List<WorkoutPlan> toEntityList(List<WorkoutPlanDTO> workoutPlanDTOList);

    @Named("exerciseListToNames")
    static List<String> exerciseListToNames(List<Exercise> exercises) {
        return exercises != null ? exercises.stream()
                .map(Exercise::getName)
                .collect(Collectors.toList()) : null;
    }
}
