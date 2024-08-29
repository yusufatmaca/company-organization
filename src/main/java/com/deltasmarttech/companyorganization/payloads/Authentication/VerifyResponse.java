package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse {

    private String email;
    private String message;
    private String expirationTime;
}
