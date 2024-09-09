package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.Authentication.AllUsersResponse;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;

public interface UserOperationService {

    AllUsersResponse getAllUsers(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder);

    UserDTO editUser(Integer userId, UserDTO userDTO);
}
