package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeRequest;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.EmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.ManagerDTO;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO departmentDTO, Integer companyId, Integer departmentTypeId, Integer townId);
	DepartmentResponse getAllDepartmentsByCompany(Integer companyId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	DepartmentDTO deleteDepartment(Integer companyId, Integer departmentId);
	EmployeeResponse showAllEmployees(Integer companyId, Integer departmentId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	DepartmentDTO assignManager(ManagerDTO manager, Integer companyId, Integer departmentId);
	DepartmentDTO deleteManager(Integer companyId, Integer departmentId);
	AddOrRemoveEmployeeResponse addEmployee(AddOrRemoveEmployeeRequest employee, Integer companyId, Integer departmentId);
	AddOrRemoveEmployeeResponse deleteEmployee(Integer employeeId, Integer companyId, Integer departmentId);
    DepartmentDTO updateDepartment(Integer companyId, Integer departmentId, DepartmentDTO departmentDTO);

	DepartmentResponse getAllDepartments(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
