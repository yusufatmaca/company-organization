package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.DeleteUserDTO;
import com.deltasmarttech.companyorganization.payloads.VerifyDTO;
import com.deltasmarttech.companyorganization.repositories.RoleRepository;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private final AuthenticationManager authManager;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final MailService mailService;
    @Autowired
    private CustomSecurityExpressionRoot securityExpressionRoot;

    public AuthenticationResponse register(RegisterRequest request) {

        // Is the e-mail, which is entered, registered in the system?
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new APIException("Error: Email is already in use!");

        /*  If the role is null, then assign "EMPLOYEE"
            If the role is not valid, then throw an exception
            If the role is valid, then assign it the `role` field in `User` entity
        */
        String roleString = request.getRole();
        AppRole appRole;
        try {
            appRole = (roleString == null || roleString.isEmpty()) ?
                    AppRole.EMPLOYEE : AppRole.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new APIException("Error: \'" + roleString + "\' is not found!");
        }

        Optional<Role> role = roleRepository.findByRoleName(appRole);

        String verificationCode = UUID.randomUUID().toString();

        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(false)
            .active(true)
            .role(role.get())
            .verificationCode(verificationCode)
            .build();
        userRepository.save(user);

        mailService.sendVerificationEmail(user.getEmail(), verificationCode);

        return AuthenticationResponse.builder()
                .email(request.getEmail())
                .roleName(appRole.name())
                .token(null)
                .enabled(user.isEnabled())
                .active(user.isActive())
                .message("Verification mail has been sent. " +
                        "Please check your mail box and enter the verification code")
                .build();
    }

    public AuthenticationResponse login(AuthenticateRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new APIException("The email you entered could not be found in the system."));

        if (!user.isEnabled()) {
            throw new APIException("Please verify your e-mail address by entering the verification code!");
        }

        if (!user.isActive()) {
            throw new APIException("Your account is inactive!");
        }

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {

            throw new APIException("Invalid password.");
        }

        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .active(user.isActive())
                .enabled(user.isEnabled())
                .roleName(user.getRole().getRoleName().name())
                .message("Login successful!")
                .build();

    }

    public AuthenticationResponse verify(VerifyDTO verify) {

        User user = userRepository.findByEmail(verify.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        if (user.getVerificationCode() != null) {
            if (user.getVerificationCode().equals(verify.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                userRepository.save(user);
            } else {
                throw new APIException("You didn't enter correctly your verification code.");
            }
        } else {
            throw new APIException("You already verified your account!");
        }
        return AuthenticationResponse.builder()
                .token(null)
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .enabled(user.isEnabled())
                .active(user.isActive())
                .message("Verification successful!")
                .build();
    }

    @PreAuthorize("@customSecurityExpressionRoot.isAdminOrAccountOwner(#userId)")
    public AuthenticationResponse deleteUser(DeleteUserDTO delete) {

        User user = userRepository.findByEmail(delete.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        if (!securityExpressionRoot.isAdminOrAccountOwner(delete.getEmail())) {
            throw new APIException("You don't have permission to delete this account");
        }

        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(null)
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .enabled(user.isEnabled())
                .active(user.isActive())
                .message("Your account has deleted!")
                .build();
    }

}
