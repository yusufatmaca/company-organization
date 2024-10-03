package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.APIResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import com.deltasmarttech.companyorganization.repositories.*;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfilePictureRepository profilePictureRepository;

    public UserOperationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CompanyRepository companyRepository, DepartmentRepository departmentRepository, ProfilePictureRepository profilePictureRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.profilePictureRepository = profilePictureRepository;
    }

    public AllUsersResponse getAllUsers(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<User> userPage = userRepository.findAll(pageDetails);

        List<UserDTO> userDTOs = userPage
                .getContent()
                .stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());

        return AllUsersResponse.builder()
                .content(userDTOs)
                .pageNumber(userPage.getNumber())
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .lastPage(userPage.isLast())
                .build();
    }

    @Override
    public UserDTO editUser(Integer userId, UserDTO userDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }
        if (userDTO.getSurname() != null) {
            user.setSurname(userDTO.getSurname());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getRoleName() != null) {
            if (userDTO.getRoleName().equalsIgnoreCase("MANAGER")) {
                Optional<Department> department = departmentRepository.findById(userDTO.getDepartmentId());
                if (department.isPresent() && department.get().getManager() != null) {
                    throw new APIException("You cannot assign a MANAGER for this department, because it has already been assigned.");
                }
            }
            user.setRole(roleRepository.findByRoleName(AppRole.valueOf(userDTO.getRoleName()))
                    .orElseThrow(() -> new APIException("Invalid role name")));
        }

        if (userDTO.getCompanyId() != null) {

            Company company = companyRepository.findById(userDTO.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", userDTO.getCompanyId()));

            if (userDTO.getDepartmentId() != null) {

                Department department = departmentRepository.findById(userDTO.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department", "id", userDTO.getDepartmentId()));

                if (!department.getCompany().getId().equals(userDTO.getCompanyId())) {
                    throw new APIException("Company / department mismatch!");
                }

                Department exDept = departmentRepository.findById(user.getDepartment().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department", "id", userDTO.getDepartmentId()));

                if (user.getRole().getRoleName().equals(AppRole.MANAGER)) {
                    if (exDept.getId() != department.getId()) {
                        if (exDept.getManager() != null && exDept.getManager().getId() == user.getId()) {
                            exDept.setManager(null);
                        }
                    }
                    department.setManager(user);
                } else if (user.getRole().getRoleName().equals(AppRole.EMPLOYEE)) {
                    if (exDept.getManager() != null && exDept.getManager().getId() == user.getId()) {
                        exDept.setManager(null);
                    }
                }
                user.setDepartment(department);

            } else {
                /*String companyName = user.getDepartment().getCompany().getName();
                Integer companyId = user.getDepartment().getCompany().getId();
                user.setDepartment(null);
                userRepository.save(user);
                return mapToUserDTO(user, companyName, companyId);

                 */
                throw new APIException("Please specify department!");
            /*
            if (user.getRole().getRoleName().equals(AppRole.MANAGER)) {
                user.getDepartment().setManager(user);
            } else if (user.getRole().getRoleName().equals(AppRole.EMPLOYEE)) {
                if (user.getDepartment().getManager() != null && user.getDepartment().getManager().getId() == user.getId()) {
                    user.getDepartment().setManager(null);
                }

             */
            }
        } else {
            if (userDTO.getDepartmentId() != null) {
                throw new APIException("You cannot perform this action because if a user is in a department, then he or she must also be in a company!");
            } else {
                user.setDepartment(null);
            }
        }


        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    public APIResponse uploadProfilePicture(MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new APIException("User not found"));

        ProfilePicture profilePicture = profilePictureRepository.findByUserId(user.getId());

        try {
            profilePicture.setProfilePicture(file.getBytes());
            profilePicture.setUser(user);
            profilePictureRepository.save(profilePicture);
            return new APIResponse("Profile photo uploaded successfully!", true);
        } catch (IOException e) {
            throw new APIException("Failed to upload profile photo " + e.getMessage());
        }
    }

    @Override
    public byte[] getMyProfilePicture() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new APIException("User not found"));

        ProfilePicture pp = profilePictureRepository.findByUserId(user.getId());

        return user.getProfilePicture().getProfilePicture();
    }

    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .companyId(user.getDepartment() != null ? user.getDepartment().getCompany().getId() : null)
                .companyName(user.getDepartment() != null ? user.getDepartment().getCompany().getName() : null)
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .active(user.isActive())
                .enabled(user.isEnabled())
                .build();
    }

    private UserDTO mapToUserDTO(User user, String companyName, Integer companyId) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName().name())
                .companyId(companyId)
                .companyName(companyName)
                .departmentId(null)
                .departmentName(null)
                .active(user.isActive())
                .enabled(user.isEnabled())
                .build();
    }

}
