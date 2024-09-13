package com.pumplog.PumpLog.mapper;

import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.model.WorkoutPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutPlanMapper {

    @Mapping(source = "user.username", target = "user")
    public WorkoutPlanDTO toDto(WorkoutPlan workoutPlan);

    @Mapping(source = "user", target = "user.username")
    public WorkoutPlan toEntity(WorkoutPlanDTO workoutPlanDTO);

    public List<WorkoutPlanDTO> toDtoList(List<WorkoutPlan> workoutPlanList);

    public List<WorkoutPlan> toEntityList(List<WorkoutPlanDTO> workoutPlanDTOList);
}
