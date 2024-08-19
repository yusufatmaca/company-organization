package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {

    private String name;
    private String surname;
    private String email;
    private String role;
    private int companyId;
    private int departmentId;

}
