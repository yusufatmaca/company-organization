package com.deltasmarttech.companyorganization.controller;

import com.deltasmarttech.companyorganization.payloads.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyTypeResponse;
import com.deltasmarttech.companyorganization.services.CompanyTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CompanyTypeController {

	@Autowired
	CompanyTypeService companyTypeService;

	@PostMapping("/executive/company-types")
	public ResponseEntity<CompanyTypeDTO> addCompanyType(@Valid @RequestBody CompanyTypeDTO companyTypeDTO) {

		CompanyTypeDTO savedCompanyTypeDTO = companyTypeService.createCompanyType(companyTypeDTO);
		return new ResponseEntity<>(savedCompanyTypeDTO, HttpStatus.CREATED);
	}

	@GetMapping("/company-types")
	public ResponseEntity<CompanyTypeResponse> getAllCompanyTypes() {
		CompanyTypeResponse allCompanyTypesResponse = companyTypeService.getAllCompanyTypes();
		return new ResponseEntity<>(allCompanyTypesResponse, HttpStatus.OK);
	}

	@DeleteMapping("/executive/company-types/{companyTypeId}")
	public ResponseEntity<CompanyTypeDTO> deleteCompanyType(@PathVariable Integer companyTypeId) {

		CompanyTypeDTO deleteCompanyTypeDTO = companyTypeService.deleteCompanyType(companyTypeId);
		return new ResponseEntity<>(deleteCompanyTypeDTO, HttpStatus.OK);
	}

}
