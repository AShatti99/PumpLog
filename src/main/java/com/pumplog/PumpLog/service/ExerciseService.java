package com.pumplog.PumpLog.service;

import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.mapper.ExerciseMapper;
import com.pumplog.PumpLog.model.Exercise;
import com.pumplog.PumpLog.model.WorkoutPlan;
import com.pumplog.PumpLog.repository.ExerciseRepository;
import com.pumplog.PumpLog.repository.WorkoutPlanRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.pumplog.PumpLog.dto.ExerciseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private HttpServletRequest request;

    public ResponseEntity<String> createExercise(Long workoutPlanId, ExerciseDTO exerciseDTO){

        log.info("[ExerciseService createExercise] Start validation");

        if(workoutPlanId == null) {
            log.error("[ExerciseService createExercise] Missing workoutPlanid");
            return ResponseEntity.badRequest().body("Missing workoutPlanid");
        }

        Optional<WorkoutPlan> workoutPlan = workoutPlanRepository.findById(workoutPlanId);
        if(workoutPlan.isEmpty()) {
            log.error("[ExerciseService createExercise] WorkoutPlan not found");
            return ResponseEntity.badRequest().body("WorkoutPlan not found");
        }

        String requestUsername = (String) request.getAttribute("username");
        String ownerWorkoutPlan = workoutPlan.get().getUser().getUsername();
        if(!ownerWorkoutPlan.equals(requestUsername)) {
            log.error("[ExerciseService createExercise] It's not your workout plan");
            return ResponseEntity.badRequest().body("It's not your workout plan");
        }

        if(exerciseDTO == null) {
            log.error("[ExerciseService createExercise] Missing exerciseDTO");
            return ResponseEntity.badRequest().body("Missing exerciseDTO");
        }

        if(StringUtils.isEmpty(exerciseDTO.getName())) {
            log.error("[ExerciseService createExercise] Missing Exercise name");
            return ResponseEntity.badRequest().body("Missing Exercise name");
        }

        if(StringUtils.isEmpty(exerciseDTO.getSets())) {
            log.error("[ExerciseService createExercise] Missing Exercise sets");
            return ResponseEntity.badRequest().body("Missing Exercise sets");
        }

        if(StringUtils.isEmpty(exerciseDTO.getBreakDuration())) {
            log.error("[ExerciseService createExercise] Missing Exercise break");
            return ResponseEntity.badRequest().body("Missing Exercise break");
        }

        log.info("[ExerciseService createExercise] End validation");

        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        if(exercise.getWorkoutPlanList() == null) exercise.setWorkoutPlanList(new java.util.ArrayList<>());
        exercise.getWorkoutPlanList().add(workoutPlan.get());
        if(workoutPlan.get().getExerciseList() == null) workoutPlan.get().setExerciseList(new java.util.ArrayList<>());
        workoutPlan.get().getExerciseList().add(exercise);

        exerciseRepository.save(exercise);

        log.info("[ExerciseService createExercise] Exercise successfully created. {}", exercise);
        return ResponseEntity.ok("[ExerciseService createExercise] Exercise successfully created. \n" + exercise);
    }

    public ResponseEntity<String> updateExercise(Long workoutPlanId, Long exerciseId, ExerciseDTO exerciseDTO){

        log.info("[ExerciseService updateExercise] Start validation");

        if(workoutPlanId == null) {
            log.error("[ExerciseService updateExercise] Missing workoutPlanId");
            return ResponseEntity.badRequest().body("Missing workoutPlanId");
        }

        Optional <WorkoutPlan> workoutPlan = workoutPlanRepository.findById(workoutPlanId);
        if(workoutPlan.isEmpty()) {
            log.error("[ExerciseService updateExercise] WorkoutPlan not found");
            return ResponseEntity.badRequest().body("WorkoutPlan not found");
        }
        String ownerWorkoutPlan = workoutPlan.get().getUser().getUsername();
        String requestUsername = (String) request.getAttribute("username");
        if(!ownerWorkoutPlan.equals(requestUsername)) {
            log.error("[ExerciseService updateExercise] It's not your workout plan");
            return ResponseEntity.badRequest().body("It's not your workout plan");
        }

        if(exerciseId == null) {
            log.error("[ExerciseService updateExercise] Missing exerciseId");
            return ResponseEntity.badRequest().body("Missing exerciseId");
        }

        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        if(exercise.isEmpty()) {
            log.error("[ExerciseService updateExercise] Exercise not found");
            return ResponseEntity.badRequest().body("Exercise not found");
        }

        if(!workoutPlan.get().getExerciseList().contains(exercise.get())) {
            log.error("[ExerciseService updateExercise] Exercise not found in workout plan");
            return ResponseEntity.badRequest().body("Exercise not found in workout plan");
        }

        log.info("[ExerciseService updateExercise] End validation");

        if(StringUtils.isNotEmpty(exerciseDTO.getName())) {
            exercise.get().setName(exerciseDTO.getName());
        }

        if(StringUtils.isNotEmpty(exerciseDTO.getSets())) {
            exercise.get().setSets(exerciseDTO.getSets());
        }

        if(StringUtils.isNotEmpty(exerciseDTO.getBreakDuration())) {
            exercise.get().setBreakDuration(exerciseDTO.getBreakDuration());
        }

        exerciseRepository.save(exercise.get());

        log.info("[ExerciseService updateExercise] Exercise successfully updated. {}", exercise.get());
        return ResponseEntity.ok("Exercise successfully updated. \n" + exercise.get());
    }


    public ResponseEntity<String> deleteExercise(Long workoutPlanId, Long exerciseId){

        log.info("[ExerciseService deleteExercise] Start validation");

        if(workoutPlanId == null) {
            log.error("[ExerciseService deleteExercise] Missing workoutPlanId");
            return ResponseEntity.badRequest().body("Missing workoutPlanId");
        }

        Optional <WorkoutPlan> workoutPlan = workoutPlanRepository.findById(workoutPlanId);
        if(workoutPlan.isEmpty()) {
            log.error("[ExerciseService deleteExercise] WorkoutPlan not found");
            return ResponseEntity.badRequest().body("WorkoutPlan not found");
        }
        String ownerWorkoutPlan = workoutPlan.get().getUser().getUsername();
        String requestUsername = (String) request.getAttribute("username");
        if(!ownerWorkoutPlan.equals(requestUsername)) {
            log.error("[ExerciseService deleteExercise] It's not your workout plan");
            return ResponseEntity.badRequest().body("It's not your workout plan");
        }

        if(exerciseId == null) {
            log.error("[ExerciseService deleteExercise] Missing exerciseId");
            return ResponseEntity.badRequest().body("Missing exerciseId");
        }

        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);

        if(exercise.isEmpty()) {
            log.error("[ExerciseService deleteExercise] Exercise not found");
            return ResponseEntity.badRequest().body("Exercise not found");
        }

        if(!workoutPlan.get().getExerciseList().contains(exercise.get())) {
            log.error("[ExerciseService deleteExercise] Exercise not found in workout plan");
            return ResponseEntity.badRequest().body("Exercise not found in workout plan");
        }

        log.info("[ExerciseService deleteExercise] End validation");

        workoutPlan.get().getExerciseList().remove(exercise.get());     // qui rimuove l'esercizio dalla tabella di join exercise_workout_plan
        workoutPlanRepository.save(workoutPlan.get());
        exerciseRepository.delete(exercise.get());                      // qui rimuove l'esercizio dalla tabella exercise

        log.info("[ExerciseService deleteExercise] Exercise with id {} successfully deleted", exerciseId);
        return ResponseEntity.ok("Exercise with id " +exerciseId+ " successfully deleted");
    }

    public ResponseEntity<List<ExerciseDTO>> getAllExercisesByWorkoutPlanId(Long workoutPlanId){

        log.info("[ExerciseService getAllExercisesByWorkoutPlanId] Start validation");

        if(workoutPlanId == null) {
            log.error("[ExerciseService getAllExercisesByWorkoutPlanId] Missing workoutPlanId");
            return ResponseEntity.badRequest().body(null);
        }

        Optional <WorkoutPlan> workoutPlan = workoutPlanRepository.findById(workoutPlanId);
        if(workoutPlan.isEmpty()) {
            log.error("[ExerciseService getAllExercisesByWorkoutPlanId] WorkoutPlan not found");
            return ResponseEntity.badRequest().body(null);
        }

        log.info("[ExerciseService getAllExercisesByWorkoutPlanId] End validation");

        List <ExerciseDTO> exerciseList = exerciseMapper.toDtoList(workoutPlan.get().getExerciseList());

        if(exerciseList.isEmpty()) {
            log.warn("[ExerciseService getAllExercisesByWorkoutPlanId] WorkoutPlan has no exercises");
        }

        return ResponseEntity.ok(exerciseList);
    }

    public ResponseEntity<Page<ExerciseDTO>> getAllExercises(String sort, String direction, int page, int size){

        log.info("[ExerciseService getAllExercises] Creating page request");

        if(StringUtils.isEmpty(sort)){
            log.info("[ExerciseService getAllExercises] Missing sort. Sorting by name");
            sort = "name";
        }

        if(StringUtils.isEmpty(direction)){
            log.info("[ExerciseService getAllExercises] Missing direction. Sorting in ascending order");
            direction = "asc";
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            log.error("[ExerciseService getAllExercises] Invalid direction: {}", direction);
            return ResponseEntity.badRequest().body(null);
        }
        Sort sortOrder = direction.equalsIgnoreCase("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Exercise> exercisePage = exerciseRepository.findAll(pageable);
        Page<ExerciseDTO> exerciseDTOPage = exercisePage.map(exerciseMapper::toDto);

        log.info("[ExerciseService getAllExercises] Exercises successfully retrieved.");
        return ResponseEntity.ok(exerciseDTOPage);
    }

    public ResponseEntity<ExerciseDTO> getExercise(Long exerciseId){

        log.info("[ExerciseService getExercise] Start validation");

        if(exerciseId == null) {
            log.error("[ExerciseService getExercise] Missing exerciseId");
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        if(exercise.isEmpty()) {
            log.error("[ExerciseService getExercise] Exercise not found");
            return ResponseEntity.badRequest().body(null);
        }

        log.info("[ExerciseService getExercise] End validation");
        return ResponseEntity.ok(exerciseMapper.toDto(exercise.get()));
    }
}
