package com.deltasmarttech.companyorganization.payloads.Department.Employee;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrRemoveEmployeeResponse {

    private String email;
    private String name;
    private String surname;
    private String role;
    private Integer departmentId;

}
