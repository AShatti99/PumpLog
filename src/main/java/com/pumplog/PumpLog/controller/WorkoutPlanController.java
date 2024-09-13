package com.pumplog.PumpLog.controller;

import com.pumplog.PumpLog.config.jwt.ValidateJwt;
import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.service.WorkoutPlanService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/workoutPlan")
public class WorkoutPlanController {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @PostMapping(value = "/create")
    @ValidateJwt
    public ResponseEntity<String> createWorkoutPlan(@RequestBody WorkoutPlanDTO workoutPlanDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController createWorkoutPlan] DTO received: {}", workoutPlanDTO);

        ResponseEntity<String> response = workoutPlanService.createWorkoutPlan(workoutPlanDTO);

        stopWatch.stop();
        log.info("[WorkoutPlanController createWorkoutPlan] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @PutMapping(value = "/update/{id}")
    @ValidateJwt
    public ResponseEntity<String> updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanDTO workoutPlanDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController updateWorkoutPlan] DTO received: {}", workoutPlanDTO);

        ResponseEntity<String> response = workoutPlanService.updateWorkoutPlan(id, workoutPlanDTO);

        stopWatch.stop();
        log.info("[WorkoutPlanController updateWorkoutPlan] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }


}
