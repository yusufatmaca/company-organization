package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

	private Integer id;
	private String name;
	private CityDTO address;
	private String addressDetail;
	private DepartmentTypeDTO departmentType;
	private String companyName;
	private String managerName;
	private List<EmployeeDTO> employees;
	private boolean active;
}
