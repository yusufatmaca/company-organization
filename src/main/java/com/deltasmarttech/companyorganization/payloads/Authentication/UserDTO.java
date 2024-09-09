package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String roleName;
    private Integer companyId;
    private String companyName;
    private Integer departmentId;
    private String departmentName;
    private boolean active;
    private boolean enabled;

}
