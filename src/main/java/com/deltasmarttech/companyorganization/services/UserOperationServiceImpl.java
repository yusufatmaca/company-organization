package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.AppRole;
import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Department;
import com.deltasmarttech.companyorganization.models.User;
import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import com.deltasmarttech.companyorganization.repositories.CompanyRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
import com.deltasmarttech.companyorganization.repositories.RoleRepository;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    public UserOperationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, CompanyRepository companyRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
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
            user.setRole(roleRepository.findByRoleName(AppRole.valueOf(userDTO.getRoleName()))
                    .orElseThrow(() -> new APIException("Invalid role name")));
        }

        if (userDTO.getCompanyId() != null) {
            if (userDTO.getDepartmentId() != null) {

                Company company = companyRepository.findById(userDTO.getCompanyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Company", "id", userDTO.getCompanyId()));

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
                String companyName = user.getDepartment().getCompany().getName();
                Integer companyId = user.getDepartment().getCompany().getId();
                user.setDepartment(null);
                return mapToUserDTO(user, companyName, companyId);
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
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .active(user.isActive())
                .enabled(user.isEnabled())
                .build();
    }

}
