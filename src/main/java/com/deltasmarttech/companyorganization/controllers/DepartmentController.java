package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.models.User;
import com.deltasmarttech.companyorganization.payloads.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentResponse;
import com.deltasmarttech.companyorganization.payloads.ManagerDTO;
import com.deltasmarttech.companyorganization.services.DepartmentService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@PostMapping("/executive/companies/{companyId}/departments")
	public ResponseEntity<DepartmentDTO> createDepartment(
			@Valid @RequestBody DepartmentDTO departmentDTO,
			@PathVariable Integer companyId,
			@RequestParam(name="departmentType", required = true) Integer departmentTypeId,
			@RequestParam(name = "town") Integer townId) {

		DepartmentDTO savedDepartmentDTO = departmentService.createDepartment(departmentDTO, companyId, departmentTypeId,townId);
		return new ResponseEntity<>(savedDepartmentDTO, HttpStatus.CREATED);
	}

	@GetMapping("companies/{companyId}/departments")
	public ResponseEntity<DepartmentResponse> getAllDepartmentsByCompany(
			@PathVariable Integer companyId,

			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,

			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		DepartmentResponse allDepartmentsByCompanyResponse = departmentService.getAllDepartmentsByCompany(companyId, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allDepartmentsByCompanyResponse, HttpStatus.OK);
	}

	@DeleteMapping("/executive/companies/{companyId}/departments/{departmentId}")
	public ResponseEntity<DepartmentDTO> deleteDepartment(@PathVariable Integer companyId, @PathVariable Integer departmentId) {

		DepartmentDTO deletedDepartmentDTO = departmentService.deleteDepartment(companyId, departmentId);
		return new ResponseEntity<>(deletedDepartmentDTO, HttpStatus.OK);
	}

	@PostMapping("/executive/companies/{companyId}/departments/{departmentId}")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<DepartmentDTO> addManager(
			@RequestBody ManagerDTO manager,
			@PathVariable Integer companyId,
			@PathVariable Integer departmentId) {

		DepartmentDTO departmentHasAManager = departmentService.addManager(manager, companyId, departmentId);
		return new ResponseEntity<>(departmentHasAManager, HttpStatus.OK);
	}
}
