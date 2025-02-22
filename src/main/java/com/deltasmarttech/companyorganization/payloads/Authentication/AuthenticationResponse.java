package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String email;
    private String name;
    private String surname;
    private String roleName;
    private String token;
    private String message;
    private boolean enabled;
    private boolean active;
    private Integer companyId;
    private String companyName;
    private Integer departmentId;
    private String departmentName;

}
