package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeResponse;

public interface DepartmentTypeService {

	DepartmentTypeDTO createDepartmentType(DepartmentTypeDTO departmentTypeDTO);

	DepartmentTypeResponse getAllDepartmentTypes();

	DepartmentTypeDTO deleteDepartmentType(Integer departmentTypeId);
}
