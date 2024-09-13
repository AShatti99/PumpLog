package com.pumplog.PumpLog.service;

import com.pumplog.PumpLog.dto.WorkoutPlanDTO;
import com.pumplog.PumpLog.mapper.WorkoutPlanMapper;
import com.pumplog.PumpLog.model.User;
import com.pumplog.PumpLog.model.WorkoutPlan;
import com.pumplog.PumpLog.repository.UserRepository;
import com.pumplog.PumpLog.repository.WorkoutPlanRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Log4j2
@Service
public class WorkoutPlanService {

    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkoutPlanMapper workoutPlanMapper;

    @Autowired
    private HttpServletRequest request;

    public ResponseEntity<String> createWorkoutPlan(WorkoutPlanDTO workoutPlanDTO){

        log.info("[WorkoutPlanService addWorkoutPlan] Start validation");

        if(workoutPlanDTO == null) return ResponseEntity.badRequest().body("WorkoutPlanDTO is null");

        String username = workoutPlanDTO.getUser();

        if(StringUtils.isEmpty(username)) {
            log.error("[WorkoutPlanService addWorkoutPlan] Missing user");
            return ResponseEntity.badRequest().body("Missing User");
        }

        User user = userRepository.findByUsername(username);
        if(user == null) {
            log.error("[WorkoutPlanService addWorkoutPlan] User not found");
            return ResponseEntity.badRequest().body("User not found");
        }

        if(StringUtils.isEmpty(workoutPlanDTO.getName())) {
            log.error("[WorkoutPlanService addWorkoutPlan] Missing name");
            return ResponseEntity.badRequest().body("Missing Name");
        }

        log.info("[WorkoutPlanService addWorkoutPlan] End validation");

        if(workoutPlanDTO.getStartDate() == null) {
            log.info("[WorkoutPlanService addWorkoutPlan] Start date is null, setting it to now");
            workoutPlanDTO.setStartDate(LocalDate.now());
        }

        WorkoutPlan workoutPlan = workoutPlanMapper.toEntity(workoutPlanDTO);
        workoutPlan.setUser(user);

        workoutPlanRepository.save(workoutPlan);

        log.info("[WorkoutPlanService addWorkoutPlan] Workoutplan successfully created: {}", workoutPlan);
        return ResponseEntity.ok("WorkoutPlan successfully created: " + workoutPlan);
    }

    public ResponseEntity<String> updateWorkoutPlan(Long id, WorkoutPlanDTO workoutPlanDTO){

        log.info("[WorkoutPlanService updateWorkoutPlan] Start validation");

        if(id == null) {
            log.error("[WorkoutPlanService updateWorkoutPlan] Missing id");
            return ResponseEntity.badRequest().body("Missing Id");
        }

        Optional<WorkoutPlan> workoutPlan = workoutPlanRepository.findById(id);
        if(workoutPlan.isEmpty()) {
            log.error("[WorkoutPlanService updateWorkoutPlan] Workoutplan not found");
            return ResponseEntity.badRequest().body("Workoutplan not found");
        }

        if(workoutPlanDTO == null) {
            log.error("[WorkoutPlanService updateWorkoutPlan] WorkoutPlanDTO is null");
            return ResponseEntity.badRequest().body("WorkoutPlanDTO is null");
        }


        String username = workoutPlanDTO.getUser();
        if(StringUtils.isEmpty(username)) {
            log.error("[WorkoutPlanService updateWorkoutPlan] Missing user");
            return ResponseEntity.badRequest().body("Missing User");
        }

        String requestUsername = (String) request.getAttribute("username");
        if(!username.equals(requestUsername)) {
            log.error("[WorkoutPlanService updateWorkoutPlan] It's not your workout plan");
            return ResponseEntity.badRequest().body("It's not your workout plan");
        }

        log.info("[WorkoutPlanService updateWorkoutPlan] End validation");

        if(StringUtils.isNotEmpty(workoutPlanDTO.getName())) {
            workoutPlan.get().setName(workoutPlanDTO.getName());
        }
        if(workoutPlanDTO.getStartDate() != null) {
            workoutPlan.get().setStartDate(workoutPlanDTO.getStartDate());
        }
        if(workoutPlanDTO.getEndDate() != null) {
            workoutPlan.get().setEndDate(workoutPlanDTO.getEndDate());
        }

        workoutPlanRepository.save(workoutPlan.get());

        log.info("[WorkoutPlanService updateWorkoutPlan] Workoutplan successfully updated: {}", workoutPlan.get());
        return ResponseEntity.ok("WorkoutPlan successfully updated: " + workoutPlan.get());
    }
}
