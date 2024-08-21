package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequest {

    // A new user must enter his/her email to be sent verify message.
    private String email;
}
