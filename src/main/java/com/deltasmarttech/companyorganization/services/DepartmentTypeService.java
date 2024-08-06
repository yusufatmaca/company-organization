package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentTypeResponse;

public interface DepartmentTypeService {

	DepartmentTypeDTO createDepartmentType(DepartmentTypeDTO departmentTypeDTO);

	DepartmentTypeResponse getAllDepartmentTypes();

	DepartmentTypeDTO deleteDepartmentType(Integer departmentTypeId);
}
