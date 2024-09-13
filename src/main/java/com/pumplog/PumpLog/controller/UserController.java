package com.pumplog.PumpLog.controller;

import com.pumplog.PumpLog.dto.UserDTO;
import com.pumplog.PumpLog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@Tag(name = "Users controller", description = "Handles REST API requests related to users")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registration")
    @Operation(summary = "Register a new user")
    private ResponseEntity<String> registration(@RequestBody UserDTO userDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[UserController registration] DTO received: {}", userDTO);

        ResponseEntity<String> response = userService.registration(userDTO);

        stopWatch.stop();
        log.info("[UserController registration] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }


    @PostMapping(value = "/login")
    @Operation(summary = "Login a user")
    private ResponseEntity<String> login(@RequestBody UserDTO userDTO){

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("[UserController login] DTO received: {}", userDTO);

        ResponseEntity<String> response = userService.login(userDTO);

        stopWatch.stop();
        log.info("[UserController login] Return time elapsed: {} ms", stopWatch.getTotalTimeMillis());

        return response;
    }

}
