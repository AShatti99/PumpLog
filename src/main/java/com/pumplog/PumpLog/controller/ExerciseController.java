package com.pumplog.PumpLog.controller;


import com.pumplog.PumpLog.config.jwt.ValidateJwt;
import com.pumplog.PumpLog.dto.ExerciseDTO;
import com.pumplog.PumpLog.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@Tag(name = "Exercise controller", description = "Handles REST API requests related to exercises")
@RequestMapping("/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping(value = "/create/{workoutPlanId}")
    @ValidateJwt
    @Operation(summary = "Create exercise in a workout plan")
    public ResponseEntity<String> createExercise(@PathVariable Long workoutPlanId, @RequestBody ExerciseDTO exerciseDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController createExercise] WorkoutPlanId received: {}, DTO received: {}",  workoutPlanId, exerciseDTO);

        ResponseEntity<String> response = exerciseService.createExercise(workoutPlanId, exerciseDTO);

        stopWatch.stop();
        log.info("[ExerciseController createExercise] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @PutMapping(value = "/update/{workoutPlanId}/{exerciseId}")
    @ValidateJwt
    @Operation(summary = "Update an exercise")
    public ResponseEntity<String> updateExercise(@PathVariable Long workoutPlanId, @PathVariable Long exerciseId, @RequestBody ExerciseDTO exerciseDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController updateExercise] WorkoutPlanId received: {}, ExerciseId received: {}, DTO received: {}", workoutPlanId, exerciseId, exerciseDTO);

        ResponseEntity<String> response = exerciseService.updateExercise(workoutPlanId, exerciseId, exerciseDTO);

        stopWatch.stop();
        log.info("[ExerciseController updateExercise] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @DeleteMapping(value = "/delete/{workoutPlanId}/{exerciseId}")
    @ValidateJwt
    @Operation(summary = "Delete an exercise")
    public ResponseEntity<String> deleteExercise(@PathVariable Long workoutPlanId, @PathVariable Long exerciseId){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController deleteExercise] WorkoutPlanId received: {}, ExerciseId received: {}", workoutPlanId, exerciseId);

        ResponseEntity<String> response = exerciseService.deleteExercise(workoutPlanId, exerciseId);

        stopWatch.stop();
        log.info("[ExerciseController deleteExercise] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @GetMapping(value = "/all/{workoutPlanId}")
    @ValidateJwt
    @Operation(summary = "Get all exercises from a workout plan")
    public ResponseEntity<List<ExerciseDTO>> getAllExercisesByWorkoutPlanId(@PathVariable Long workoutPlanId){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController getAllExercisesByWorkoutPlanId] WorkoutPlanId received: {}", workoutPlanId);

        ResponseEntity<List<ExerciseDTO>> response = exerciseService.getAllExercisesByWorkoutPlanId(workoutPlanId);

        stopWatch.stop();
        log.info("[ExerciseController getAllExercisesByWorkoutPlanId] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @GetMapping(value = "/all")
    @ValidateJwt
    @Operation(summary = "Get all exercises paged")
    public ResponseEntity<Page<ExerciseDTO>> getAllExercises(@RequestParam(required = false) String sort, @RequestParam(required = false) String direction,
                                                             @RequestParam Integer page, @RequestParam Integer size){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController getAllExercises] Sort: '{}', Direction: '{}', Page: '{}', Size: '{}'", sort, direction, page, size);

        ResponseEntity<Page<ExerciseDTO>> response = exerciseService.getAllExercises(sort, direction, page, size);

        stopWatch.stop();
        log.info("[ExerciseController getAllExercises] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

    @GetMapping(value = "/{exerciseId}")
    @ValidateJwt
    @Operation(summary = "Get an exercise")
    public ResponseEntity<ExerciseDTO> getExercise(Long exerciseId) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[ExerciseController getExercise] ExerciseId received: {}", exerciseId);

        ResponseEntity<ExerciseDTO> response = exerciseService.getExercise(exerciseId);

        stopWatch.stop();
        log.info("[ExerciseController getExercise] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

}
