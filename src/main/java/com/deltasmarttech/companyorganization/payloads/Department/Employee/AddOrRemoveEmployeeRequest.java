package com.deltasmarttech.companyorganization.payloads.Department.Employee;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddOrRemoveEmployeeRequest {

    @Email
    private String email;
}
