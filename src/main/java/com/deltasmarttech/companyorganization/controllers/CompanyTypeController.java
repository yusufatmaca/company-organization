package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeResponse;
import com.deltasmarttech.companyorganization.services.CompanyTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://company-organization-software-gamma.vercel.app/", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class CompanyTypeController {

	@Autowired
	CompanyTypeService companyTypeService;

	@PostMapping("/admin/company-types")
	public ResponseEntity<CompanyTypeDTO> addCompanyType(@Valid @RequestBody CompanyTypeDTO companyTypeDTO) {

		CompanyTypeDTO savedCompanyTypeDTO = companyTypeService.createCompanyType(companyTypeDTO);
		return new ResponseEntity<>(savedCompanyTypeDTO, HttpStatus.CREATED);
	}

	@GetMapping("/public/company-types")
	public ResponseEntity<CompanyTypeResponse> getAllCompanyTypes() {
		CompanyTypeResponse allCompanyTypesResponse = companyTypeService.getAllCompanyTypes();
		return new ResponseEntity<>(allCompanyTypesResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/company-types/{companyTypeId}")
	public ResponseEntity<CompanyTypeDTO> deleteCompanyType(@PathVariable Integer companyTypeId) {

		CompanyTypeDTO deleteCompanyTypeDTO = companyTypeService.deleteCompanyType(companyTypeId);
		return new ResponseEntity<>(deleteCompanyTypeDTO, HttpStatus.OK);
	}

}
