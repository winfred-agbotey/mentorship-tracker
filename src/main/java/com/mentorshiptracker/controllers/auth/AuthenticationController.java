package com.mentorshiptracker.controllers.auth;

import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AdvisorDTO;
import com.mentorshiptracker.dtos.ResponseHandler;
import com.mentorshiptracker.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/v1/advisor")
    public ResponseEntity<Object> registerAdvisor(@Valid @RequestBody AdvisorDTO requestDTO) {
        return ResponseHandler.successResponse(HttpStatus.CREATED, authService.registerAdvisor(requestDTO));
    }

    @PostMapping("/v1/authenticate")
    public ResponseEntity<Object> authenticate(@Valid @RequestBody AuthRequest authRequestDTO) {
        return ResponseHandler.successResponse(HttpStatus.OK, authService.authenticate(authRequestDTO));
    }
}
