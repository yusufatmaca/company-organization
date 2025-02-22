package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserResponse {

    private Integer id;
    private String email;
    private String companyName;
    private String departmentName;
    private String role;
    private String message;
}
