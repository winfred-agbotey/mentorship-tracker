package com.mentorshiptracker.controllers;

import com.mentorshiptracker.dtos.AdviseeDTO;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.ResponseHandler;
import com.mentorshiptracker.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class UserController {
    private final UserService userService;

    @PostMapping("v1/users/advisor")
    public ResponseEntity<Object> createAdvisor(@Valid @RequestBody AdvisorDTO requestDTO){
        return ResponseHandler.successResponse(HttpStatus.CREATED,userService.createAdvisor(requestDTO));
    }

    @PostMapping("v1/users/advisee")
    public ResponseEntity<Object> createAdvisee(@Valid @RequestBody AdviseeDTO requestDTO){
        return ResponseHandler.successResponse(HttpStatus.CREATED,userService.createAdvisee(requestDTO));
    }

}
