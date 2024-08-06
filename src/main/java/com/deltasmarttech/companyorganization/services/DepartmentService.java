package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.ManagerDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Integer companyId, Integer departmentTypeId, Integer townId);

	DepartmentResponse getAllDepartmentsByCompany(Integer companyId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	DepartmentDTO deleteDepartment(Integer companyTypeId, Integer departmentId);


	DepartmentDTO addManager(ManagerDTO user, Integer companyId, Integer departmentId);
}
