package com.deltasmarttech.companyorganization.payloads.CompanyType;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyTypeDTO {

	private Integer id;
	private String name;
	private boolean active;
}
