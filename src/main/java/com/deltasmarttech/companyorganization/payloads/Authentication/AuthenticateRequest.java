package com.deltasmarttech.companyorganization.payloads.Authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {

    @NotBlank(message = "Email field must be filled")
    private String email;

    @NotBlank(message = "Password field must be filled")
    private String password;
}
