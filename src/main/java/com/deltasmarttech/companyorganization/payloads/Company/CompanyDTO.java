package com.deltasmarttech.companyorganization.payloads.Company;

import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {

	private Integer id;
	private String name;
	private String shortName;
	private String city;
	private String region;
	private String town;
	private CompanyTypeDTO companyType;
	private String addressDetail;
	private boolean active = true;

}