package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.models.AuthenticateRequest;
import com.deltasmarttech.companyorganization.models.AuthenticationResponse;
import com.deltasmarttech.companyorganization.models.RegisterRequest;
import com.deltasmarttech.companyorganization.payloads.VerifyDTO;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestBody VerifyDTO verify) {
        return ResponseEntity.ok(authService.verify(verify));
    }
}
