package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentTypeResponse;
import com.deltasmarttech.companyorganization.services.DepartmentTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class DepartmentTypeController {

	@Autowired
	private DepartmentTypeService departmentTypeService;

	@PostMapping("/admin/department-types")
	public ResponseEntity<DepartmentTypeDTO> createDepartmentType(@Valid @RequestBody DepartmentTypeDTO departmentTypeDTO) {

		DepartmentTypeDTO savedDepartmentTypeDTO = departmentTypeService.createDepartmentType(departmentTypeDTO);
		return new ResponseEntity<>(savedDepartmentTypeDTO, HttpStatus.CREATED);
	}

	@GetMapping("/admin/department-types")
	public ResponseEntity<DepartmentTypeResponse> getAllDepartmentTypes() {

		DepartmentTypeResponse allDepartmentTypesResponse = departmentTypeService.getAllDepartmentTypes();
		return new ResponseEntity<>(allDepartmentTypesResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/department-types/{departmentTypeId}")
	public ResponseEntity<DepartmentTypeDTO> deleteDepartmentType(@PathVariable Integer departmentTypeId) {

		DepartmentTypeDTO deletedDepartmentTypeDTO = departmentTypeService.deleteDepartmentType(departmentTypeId);
		return new ResponseEntity<>(deletedDepartmentTypeDTO, HttpStatus.OK);
	}
}
