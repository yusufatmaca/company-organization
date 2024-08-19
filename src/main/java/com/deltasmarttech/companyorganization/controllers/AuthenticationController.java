package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.models.AuthenticateRequest;
import com.deltasmarttech.companyorganization.models.AuthenticationResponse;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/admin/add-user")
    public ResponseEntity<AddUserResponse> addUser(
            @RequestBody AddUserRequest request) {
        return ResponseEntity.ok(authService.addUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<VerifyResponse> activateAccount(@RequestBody ActivateRequest activate) {
        return ResponseEntity.ok(authService.activateAccount(activate));
    }

    @PostMapping("/users/delete")
    public ResponseEntity<AuthenticationResponse> deleteUser(@RequestBody DeleteUserDTO delete) {
        return ResponseEntity.ok(authService.deleteUser(delete));
    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logout() {

        ResponseCookie cookie = jwtUtil.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been logged out!");
    }
}
