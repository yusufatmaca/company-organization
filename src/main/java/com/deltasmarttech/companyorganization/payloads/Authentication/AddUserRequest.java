package com.deltasmarttech.companyorganization.payloads.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest {

    @NonNull
    @NotBlank(message = "Name field must be filled")
    private String name;

    @NonNull
    @NotBlank(message = "Surname field must be filled")
    private String surname;

    @NonNull
    @NotBlank(message = "Email field must be filled")
    @Email
    private String email;

    private String role;

    @NotNull(message = "Company ID must be provided")
    @Min(value = 1, message = "Company ID must be a positive number")
    private int companyId;

    @NotNull(message = "Department ID must be provided")
    @Min(value = 1, message = "Department ID must be a positive number")
    private int departmentId;

}