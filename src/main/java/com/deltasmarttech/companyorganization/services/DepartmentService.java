package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Department;
import com.deltasmarttech.companyorganization.models.User;
import com.deltasmarttech.companyorganization.payloads.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.EmployeeDTO;
import com.deltasmarttech.companyorganization.payloads.ManagerDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Integer companyId, Integer departmentTypeId, Integer townId);
	DepartmentResponse getAllDepartmentsByCompany(Integer companyId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	DepartmentDTO deleteDepartment(Integer companyId, Integer departmentId);
	DepartmentDTO assignManager(ManagerDTO user, Integer companyId, Integer departmentId);
	//Department addEmployee(User employee, Department department, Company company);
	//Department deleteEmployee(User employee, Department department, Company company);
	//DepartmentDTO processEmployee(EmployeeDTO employee, Integer companyId, Integer departmentId, Integer operationType);
}
