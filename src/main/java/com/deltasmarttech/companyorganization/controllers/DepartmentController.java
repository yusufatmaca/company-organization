package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.models.User;
import com.deltasmarttech.companyorganization.payloads.*;
import com.deltasmarttech.companyorganization.payloads.Authentication.UserDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeRequest;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.EmployeeResponse;
import com.deltasmarttech.companyorganization.repositories.UserRepository;
import com.deltasmarttech.companyorganization.services.DepartmentService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://company-organization-software-gamma.vercel.app/", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@GetMapping("/admin/departments")
	public ResponseEntity<DepartmentResponse> getAllDepartments(
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		DepartmentResponse allDepartmentsByCompanyResponse = departmentService.getAllDepartments(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allDepartmentsByCompanyResponse, HttpStatus.OK);
	}

	@PostMapping("/admin/companies/{companyId}/departments")
	public ResponseEntity<DepartmentDTO> createDepartment(
			@Valid @RequestBody DepartmentDTO departmentDTO,
			@PathVariable Integer companyId,
			@RequestParam(name="departmentType", required = true) Integer departmentTypeId,
			@RequestParam(name = "town") Integer townId) {

		DepartmentDTO savedDepartmentDTO = departmentService.createDepartment(departmentDTO, companyId, departmentTypeId,townId);
		return new ResponseEntity<>(savedDepartmentDTO, HttpStatus.CREATED);
	}

	@GetMapping("/admin/companies/{companyId}/departments")
	public ResponseEntity<DepartmentResponse> getAllDepartmentsByCompany(
			@PathVariable Integer companyId,
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		DepartmentResponse allDepartmentsByCompanyResponse = departmentService.getAllDepartmentsByCompany(companyId, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allDepartmentsByCompanyResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/companies/{companyId}/departments/{departmentId}")
	public ResponseEntity<DepartmentDTO> deleteDepartment(
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {

		DepartmentDTO deletedDepartmentDTO = departmentService.deleteDepartment(companyId, departmentId);
		return new ResponseEntity<>(deletedDepartmentDTO, HttpStatus.OK);
	}

	@PostMapping("/admin/companies/{companyId}/departments/{departmentId}/manager")
	public ResponseEntity<DepartmentDTO> assignManager(
			@RequestBody ManagerDTO manager,
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {

		DepartmentDTO departmentHasAManager = departmentService.assignManager(manager, companyId, departmentId);
		return new ResponseEntity<>(departmentHasAManager, HttpStatus.CREATED);
	}

	@DeleteMapping("/admin/companies/{companyId}/departments/{departmentId}/manager")
	public ResponseEntity<DepartmentDTO> deleteManager(
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {

		DepartmentDTO departmentHasAManager = departmentService.deleteManager(companyId, departmentId);
		return new ResponseEntity<>(departmentHasAManager, HttpStatus.CREATED);
	}


	@PostMapping("/manager/companies/{companyId}/departments/{departmentId}/employees")
	public ResponseEntity<AddOrRemoveEmployeeResponse> addEmployee(
			@RequestBody AddOrRemoveEmployeeRequest employee,
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {
		AddOrRemoveEmployeeResponse addOrRemoveEmployeeResponse = departmentService.addEmployee(
				employee,
				companyId,
				departmentId);

		return new ResponseEntity<>(addOrRemoveEmployeeResponse, HttpStatus.OK);
	}

	@GetMapping("/manager/companies/{companyId}/departments/{departmentId}/addable-users")
	public ResponseEntity<EmployeeResponse> showAllAddableUsers(
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId,
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
	) {
		EmployeeResponse employeeResponse = departmentService.showAllAddableUsers(
				companyId,
				departmentId,
				pageNumber,
				pageSize,
				sortBy,
				sortOrder);

		return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
	}


	@DeleteMapping("/manager/companies/{companyId}/departments/{departmentId}/employees/{employeeId}")
	public ResponseEntity<AddOrRemoveEmployeeResponse> deleteEmployee(
			@PathVariable Integer employeeId,
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {

		AddOrRemoveEmployeeResponse employeeOperation = departmentService.deleteEmployee(
				employeeId,
				companyId,
				departmentId);

		return new ResponseEntity<>(employeeOperation, HttpStatus.OK);
	}

	@GetMapping("/manager/companies/{companyId}/departments/{departmentId}/employees")
	public ResponseEntity<EmployeeResponse> showAllEmployees(
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId,
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
	) {

		EmployeeResponse employeeResponse = departmentService.showAllEmployees(
				companyId,
				departmentId,
				pageNumber,
				pageSize,
				sortBy,
				sortOrder);

		return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
	}

	@PutMapping("/admin/companies/{companyId}/departments/{departmentId}")
	public ResponseEntity<DepartmentDTO> updateDepartment(
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId,
			@RequestBody DepartmentDTO departmentDTO) {

		DepartmentDTO updatedDepartmentDTO = departmentService.updateDepartment(companyId, departmentId, departmentDTO);
		return new ResponseEntity<>(updatedDepartmentDTO, HttpStatus.OK);
	}


}
