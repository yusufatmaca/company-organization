package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.APIResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import com.deltasmarttech.companyorganization.services.UserOperationService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "https://company-organization-software-gamma.vercel.app/", allowCredentials = "true", maxAge = 3600)
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

    @PostMapping("/profile/upload-profile-picture")
    public ResponseEntity<APIResponse> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(userOperationService.uploadProfilePicture(file), HttpStatus.OK);
    }

    @GetMapping("/profile/profile-picture")
    public ResponseEntity<byte[]> getMyProfilePicture() {
        byte[] profilePicture = userOperationService.getMyProfilePicture();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(profilePicture);

    }
}