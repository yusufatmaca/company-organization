package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.repositories.CompanyRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
import com.deltasmarttech.companyorganization.repositories.RoleRepository;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public AddUserResponse addUser(AddUserRequest request) {

        // Is the e-mail, which is entered, registered in the system?
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new APIException("Error: Email is already in use!");

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company", "id", request.getCompanyId()));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        if (!(checkForDepartmentInCompany(company, department))) {
            throw new APIException(department.getName() + " does not belong " + company.getName() + "!");
        }

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

        if(appRole.name().equalsIgnoreCase("MANAGER")) {
            checkForManagerInEmployees(department);
        }

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .department(department)
                .password("")
                .enabled(false)
                .active(true)
                .role(role.get())
                .build();
        userRepository.save(user);

        department.getEmployees().add(user);
        department.setManager(user);
        departmentRepository.save(department);

        company.getDepartments().add(department);
        companyRepository.save(company);

        return AddUserResponse.builder()
                .email(request.getEmail())
                .role(appRole.name())
                .message("User has been registered. " +
                        "Please contact " + user.getName() + " " + user.getSurname() +
                        " to verify her / his account.")
                .companyName(user.getDepartment().getCompany().getName())
                .departmentName(user.getDepartment().getName())
                .build();
    }

    public void checkForManagerInEmployees(Department department) {
        List<User> employees = department.getEmployees();

        for (User employee : employees) {
            if (employee.getRole().getRoleName().name().equals("MANAGER")) {
                throw new APIException("The department has already a 'MANAGER'!");
            }
        }
    }

    public boolean checkForDepartmentInCompany(Company company, Department department) {
        List<Department> departments = company.getDepartments();
        boolean doesExist = false;

        for (Department department1 : departments) {
            if (department1.getName().equalsIgnoreCase(department.getName())) {
                doesExist = true;
                break;
            }
        }
        return doesExist;
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


    public VerifyResponse activateAccount(ActivateRequest verify) {

        User user = userRepository.findByEmail(verify.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        if (user.isEnabled()) {
            throw new APIException("User has been already verified.");
        }

        /* if (user.getVerificationCode() != null) {
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

         */

        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        userRepository.save(user);

        mailService.sendVerificationEmail(user.getEmail(), verificationCode);
        return null;
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
