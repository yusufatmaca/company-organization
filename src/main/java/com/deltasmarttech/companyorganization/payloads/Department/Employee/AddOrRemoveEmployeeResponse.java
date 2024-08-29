package com.deltasmarttech.companyorganization.payloads.Department.Employee;

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
}
