package com.deltasmarttech.companyorganization.payloads.Department;

import com.deltasmarttech.companyorganization.payloads.Address.AddressDTO;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

	private Integer id;
	private Integer companyId;
	private String name;
	private String city;
	private String region;
	private String town;
	private String addressDetail;
	private Integer managerId;
	private DepartmentTypeDTO departmentType;
	private boolean active;
}
