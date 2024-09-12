package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.Address.City.CityResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.AuthenticateRequest;
import com.deltasmarttech.companyorganization.payloads.Authentication.AuthenticationResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.*;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

    private final AuthenticationService authService;
    private JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationService authService, JwtUtil jwtUtil) {

        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // An `admin` can add new users, whose role is `manager` or `employee`.
    @PostMapping("/admin/add-user")
    public ResponseEntity<AddUserResponse> addUser(
            @RequestBody @Valid AddUserRequest request) {
        return ResponseEntity.ok(authService.addUser(request));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AddUserResponse> register(
            @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/auth/activate-account")
    public ResponseEntity<VerifyResponse> activateAccount(@RequestBody ActivateRequest activate) throws MessagingException {
        return ResponseEntity.ok(authService.activateAccount(activate));
    }

    @PostMapping("/auth/set-password")
    public ResponseEntity<AuthenticationResponse> setPassword(
            @RequestParam("token") String token,
            @RequestParam("type") Integer type,
            @RequestBody PasswordRequest passwordRequest) {

        /*
            Type-1 -> activation process
            Type-2 -> reset password
         */

        return ResponseEntity.ok(authService.setPassword(token, passwordRequest, type));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<VerifyResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @DeleteMapping("/auth/users/delete")
    public ResponseEntity<AuthenticationResponse> deleteUser(@RequestBody DeleteUserDTO delete) {
        return ResponseEntity.ok(authService.deleteUser(delete));
    }

    @PostMapping("/auth/users/logout")
    public ResponseEntity<String> logout() {

        ResponseCookie cookie = jwtUtil.getCleanJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("You've been logged out!");
    }
}