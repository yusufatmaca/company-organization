package com.deltasmarttech.companyorganization.payloads;

import com.deltasmarttech.companyorganization.models.Role;
import lombok.Data;

@Data
public class SignupDTO {

	private String email;
	private String password;
	private String name;
	private String surname;
	private String role;

}
