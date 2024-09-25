package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.APIResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserOperationService {

    AllUsersResponse getAllUsers(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder);

    UserDTO editUser(Integer userId, UserDTO userDTO);

    APIResponse uploadProfilePicture(MultipartFile file);

    byte[] getMyProfilePicture();
}
