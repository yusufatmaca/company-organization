package com.deltasmarttech.companyorganization.payloads.Authentication;

import lombok.Data;

@Data
public class SignupDTO {

	private String email;
	private String password;
	private String name;
	private String surname;
	private String role;

}
