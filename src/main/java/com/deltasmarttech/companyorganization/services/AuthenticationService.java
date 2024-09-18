package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.Authentication.*;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.repositories.*;
import com.deltasmarttech.companyorganization.util.EmailConfirmationToken;
import com.deltasmarttech.companyorganization.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final CustomSecurityExpressionRoot securityExpressionRoot;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            AuthenticationManager authManager,
            RoleRepository roleRepository,
            MailService mailService,
            CustomSecurityExpressionRoot securityExpressionRoot,
            CompanyRepository companyRepository,
            DepartmentRepository departmentRepository,
            EmailConfirmationTokenRepository emailConfirmationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.securityExpressionRoot = securityExpressionRoot;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public AddUserResponse addUser(AddUserRequest request) {

        // Is the e-mail, which is entered, registered in the system?
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new APIException("Error: Email is already in use!");

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Company", "id", request.getCompanyId()));

        if (!company.isActive()) {
            throw new APIException("This company is inactive!");
        }

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
            throw new APIException("Error: '" + roleString + "' is not found!");
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
        if (appRole.name().equalsIgnoreCase("MANAGER")) {
            department.setManager(user);
        }
        departmentRepository.save(department);

        company.getDepartments().add(department);
        companyRepository.save(company);

        return AddUserResponse.builder()
                .id(user.getId())
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

        if(!department.isActive()) {
            throw new APIException("The department is inactive. You cannot perform this action.");
        }

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
                .name(user.getName())
                .surname(user.getSurname())
                .companyName(user.getDepartment().getCompany().getName())
                .departmentName(user.getDepartment().getName())
                .enabled(user.isEnabled())
                .roleName(user.getRole().getRoleName().name())
                .message("Login successful!")
                .companyId(user.getDepartment().getCompany() != null ? user.getDepartment().getCompany().getId() : null)
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .build();

    }

    public VerifyResponse activateAccount(ActivateRequest activate) throws MessagingException {

        // Does the user exist?
        User user = userRepository.findByEmail(activate.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        // Is the user already verified?
        if (user.isEnabled()) {
            throw new APIException("User has been already verified.");
        }

        // Generate the token
        EmailConfirmationToken emailConfirmationToken = createToken(user);

        String formattedExpirationTime = formattedDateTime(emailConfirmationToken.getExpiresAt().toString());

        // Send the token via mail
        mailService.sendVerificationEmail(emailConfirmationToken, formattedExpirationTime);


        return VerifyResponse.builder()
                .email(user.getEmail())
                .message("Please check your email to verify your account and set the password.")
                .expirationTime(formattedExpirationTime)
                .build();
    }

    private String formattedDateTime(String originalDateTime) {
        String datePart = originalDateTime.substring(0, 10);
        String timePart = originalDateTime.substring(11, 16);

        String[] dateParts = datePart.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        return day + "/" + month + "/" + year + " " + timePart;
    }

    @PreAuthorize("@customSecurityExpressionRoot.isAdminOrAccountOwner(#userId)")
    public AuthenticationResponse deleteUser(DeleteUserDTO delete) {

        User user = userRepository.findByEmail(delete.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        if (!securityExpressionRoot.isAdminOrAccountOwner(delete.getEmail())) {
            throw new APIException("You don't have permission to delete this account");
        }

        Department dept;
        if (user.getRole().getRoleName().name().equalsIgnoreCase("MANAGER")) {
            dept = user.getDepartment();
            dept.setManager(null);
            departmentRepository.save(dept);
        }

        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        user.setDepartment(null);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(null)
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .enabled(user.isEnabled())
                .active(user.isActive())
                .message("This account has been deleted!")
                .build();
    }

    @Transactional
    public AuthenticationResponse setPassword(String verificationCode, PasswordRequest passwordRequest, Integer type) {

        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new APIException("This token is not valid."));

        if(type == 1 && emailConfirmationToken.getUser().isEnabled()) {
            throw new APIException("You already activated your account.");
        }

        if (type == 2 && !emailConfirmationToken.getUser().isEnabled()) {
            throw new APIException("First of all, please activate your account.");
        }

        if (!passwordRequest.getPassword().equals(passwordRequest.getPasswordAgain())) {
            throw new APIException("The passwords you entered do not match!");
        }

        User user = emailConfirmationToken.getUser();

        if(emailConfirmationToken.isExpired()) {
            throw new APIException("The shared link for your access has expired. Please send again verification code.");
        }

        if (!isValidPassword(passwordRequest.getPassword())) {
            throw new APIException("Your password does not meet the password rules!");
        }

        if (passwordEncoder.matches(passwordRequest.getPassword(), user.getPassword())) {
            throw new APIException("The new password cannot be the same as the old password!");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        user.setEnabled(true);
        user.setEmailConfirmationToken(null);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(null)
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .enabled(user.isEnabled())
                .active(user.isActive())
                .message(user.getName() + " has set successfully password.")
                .build();
    }

    private boolean isValidPassword(String password) {

        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$";
        final Pattern pattern = Pattern.compile(passwordPattern);

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public AddUserResponse register(CreateAdminRequest request) {

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

        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .active(true)
                .role(role.get())
                .build();
        userRepository.save(user);


        return AddUserResponse.builder()
                .email(request.getEmail())
                .role(appRole.name())
                .build();
    }

    public VerifyResponse resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException {

        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new APIException("User not found"));

        if (!user.isEnabled()) {
            throw new APIException("User has not been verified. Please first verify your account.");
        }

        if (!user.isActive()) {
            throw new APIException("This account is not active. You cannot reset the password.");
        }

        Optional<EmailConfirmationToken> checkExist = emailConfirmationTokenRepository.findByUserId(user.getId());

        EmailConfirmationToken emailConfirmationToken = createToken(user);

        String formattedExpirationTime = formattedDateTime(emailConfirmationToken.getExpiresAt().toString());
        mailService.sendResetPasswordEmail(emailConfirmationToken, formattedExpirationTime);

        return VerifyResponse.builder()
                .email(user.getEmail())
                .message("Please check your email to reset the password.")
                .expirationTime(formattedExpirationTime)
                .build();
    }

    private EmailConfirmationToken createToken(User user) {

        Optional<EmailConfirmationToken> existingToken = emailConfirmationTokenRepository.findByUserId(user.getId());
        if (existingToken.isPresent()) {
            if (existingToken.get().isExpired()) {
                user.setEmailConfirmationToken(null);
                userRepository.save(user);
            } else {
                String formattedExpirationTime = formattedDateTime(existingToken.get().getExpiresAt().toString());
                throw new APIException("You already have a pending verification code to activate your account. You can only receive a new verification code after " + formattedExpirationTime);
            }

        }

        String verificationCode = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(user, verificationCode);
        emailConfirmationTokenRepository.save(emailConfirmationToken);
        return emailConfirmationToken;

    }
}
