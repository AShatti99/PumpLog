package com.pumplog.PumpLog.controller;

import com.pumplog.PumpLog.config.jwt.ValidateJwt;
import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.service.WorkoutPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@Tag(name = "Workout Plan controller", description = "Handles REST API requests related to workout plans")
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
    @Operation(summary = "Get all workout plans")
    public ResponseEntity<String> updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanDTO workoutPlanDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController updateWorkoutPlan] Id received: {}, DTO received: {}", id, workoutPlanDTO);

        ResponseEntity<String> response = workoutPlanService.updateWorkoutPlan(id, workoutPlanDTO);

        stopWatch.stop();
        log.info("[WorkoutPlanController updateWorkoutPlan] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @DeleteMapping(value = "/delete/{id}")
    @ValidateJwt
    @Operation(summary = "Delete a workout plan")
    public ResponseEntity<String> deleteWorkoutPlan(@PathVariable Long id){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController deleteWorkoutPlan] Id received: {}", id);

        ResponseEntity<String> response = workoutPlanService.deleteWorkoutPlan(id);

        stopWatch.stop();
        log.info("[WorkoutPlanController deleteWorkoutPlan] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @GetMapping(value = "/all")
    @ValidateJwt
    @Operation(summary = "Get all workout plans paginated")
    public ResponseEntity<Page<WorkoutPlanDTO>> getAllWorkoutPlans(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                                                                   @RequestParam Integer page, @RequestParam Integer size){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController getAllWorkoutPlans] Sort: '{}', Direction: '{}', Page: '{}', Size: '{}'", sort, direction, page, size);

        ResponseEntity<Page<WorkoutPlanDTO>> response = workoutPlanService.getAllWorkoutPlans(sort, direction, page, size);

        stopWatch.stop();
        log.info("[WorkoutPlanController getAllWorkoutPlans] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @GetMapping(value = "/{id}")
    @ValidateJwt
    @Operation(summary = "Get a workout plan")
    public ResponseEntity<WorkoutPlanDTO> getWorkoutPlan(@PathVariable Long id){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[WorkoutPlanController getWorkoutPlan] Id received: {}", id);

        ResponseEntity<WorkoutPlanDTO> response = workoutPlanService.getWorkoutPlan(id);

        stopWatch.stop();
        log.info("[WorkoutPlanController getWorkoutPlan] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }


}
