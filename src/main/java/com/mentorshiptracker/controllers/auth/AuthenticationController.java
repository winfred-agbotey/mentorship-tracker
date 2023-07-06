package com.mentorshiptracker.controllers.auth;

import com.mentorshiptracker.dtos.AuthRequest;
import com.mentorshiptracker.dtos.AuthenticationResponse;
import com.mentorshiptracker.dtos.RegisterAdvisorDTO;
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

    @PostMapping("/advisor")
    public ResponseEntity<AuthenticationResponse> registerAdvisor(@Valid @RequestBody RegisterAdvisorDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerAdvisor(requestDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthRequest authRequestDTO) {
        return ResponseEntity.ok(authService.authenticate(authRequestDTO));
    }
}
