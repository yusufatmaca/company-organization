package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.models.AuthenticateRequest;
import com.deltasmarttech.companyorganization.models.AuthenticationResponse;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
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
            @RequestBody AddUserRequest request) {
        return ResponseEntity.ok(authService.addUser(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AddUserResponse> register(
            @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/activate-account")
    public ResponseEntity<VerifyResponse> activateAccount(@RequestBody ActivateRequest activate) throws MessagingException {
        return ResponseEntity.ok(authService.activateAccount(activate));
    }

    @PostMapping("/set-password")
    public ResponseEntity<AuthenticationResponse> setPassword(@RequestParam("token") String token, @RequestBody PasswordRequest passwordRequest) {
        return ResponseEntity.ok(authService.setPassword(token, passwordRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticateRequest request) {
        return ResponseEntity.ok(authService.login(request));
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
