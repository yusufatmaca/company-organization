package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdminRequest {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;
}
