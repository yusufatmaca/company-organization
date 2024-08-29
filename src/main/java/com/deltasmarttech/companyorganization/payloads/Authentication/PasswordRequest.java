package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

    private String password;
    private String passwordAgain;
}
