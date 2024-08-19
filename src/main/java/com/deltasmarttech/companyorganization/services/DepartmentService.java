package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.EmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.ManagerDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Integer companyId, Integer departmentTypeId, Integer townId);
	DepartmentResponse getAllDepartmentsByCompany(Integer companyId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	DepartmentDTO deleteDepartment(Integer companyId, Integer departmentId);
	DepartmentDTO assignManager(ManagerDTO user, Integer companyId, Integer departmentId);

	EmployeeResponse showAllEmployees(Integer companyId, Integer departmentId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	//Department addEmployee(User employee, Department department, Company company);
	//Department deleteEmployee(User employee, Department department, Company company);
	//DepartmentDTO processEmployee(EmployeeDTO employee, Integer companyId, Integer departmentId, Integer operationType);
}
