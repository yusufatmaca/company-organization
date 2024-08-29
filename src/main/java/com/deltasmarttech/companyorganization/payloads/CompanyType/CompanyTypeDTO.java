package com.deltasmarttech.companyorganization.payloads.CompanyType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTypeDTO {

	private Integer id;
	private String name;
	private boolean active;

}
