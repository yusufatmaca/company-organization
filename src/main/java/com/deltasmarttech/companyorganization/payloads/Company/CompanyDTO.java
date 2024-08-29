package com.deltasmarttech.companyorganization.payloads.Company;

import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

	private Integer id;
	private String name;
	private String shortName;
	private CompanyTypeDTO companyType;
	private List<DepartmentDTO> departments;
	private String addressDetail;
	private boolean active = true;

}