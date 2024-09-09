package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import com.deltasmarttech.companyorganization.services.AuthenticationService;
import com.deltasmarttech.companyorganization.services.UserOperationService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserOperationsController {

    private final UserOperationService userOperationService;

    public UserOperationsController(UserOperationService userOperationService) {
        this.userOperationService = userOperationService;
    }

    @GetMapping("/admin/show-all-users")
    public ResponseEntity<AllUsersResponse> showAllUsers(
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        AllUsersResponse allUsersResponse = userOperationService.getAllUsers(
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return new ResponseEntity<>(allUsersResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/users/{userId}")
    public ResponseEntity<UserDTO> editUser(
            @PathVariable Integer userId,
            @RequestBody UserDTO userDTO) {

        return new ResponseEntity<>(userOperationService.editUser(userId, userDTO), HttpStatus.OK);

    }

}
