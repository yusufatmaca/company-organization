package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.repositories.RoleRepository;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new APIException("Error: Email is already in use!");

        String roleString = request.getRole();
        AppRole appRole = (roleString == null || roleString.isEmpty()) ? AppRole.EMPLOYEE : AppRole.valueOf(roleString.toUpperCase());
        Role role = roleRepository.findByRoleName(appRole)
            .orElseThrow(() -> new APIException("Error: Role is not found."));

        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .build();
        userRepository.save(user);


        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .email(request.getEmail())
                .roleName(appRole.name())
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
