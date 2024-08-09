package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyResponse;
import com.deltasmarttech.companyorganization.services.CompanyService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@PostMapping("/admin/companies")
	public ResponseEntity<CompanyDTO> createCompany(
			@RequestBody CompanyDTO companyDTO,
			@RequestParam(name="companyType", required = true) Integer companyTypeId,
			@RequestParam(name = "town", required = true) Integer townId) {

		CompanyDTO savedCompanyDTO = companyService.createCompany(companyDTO, companyTypeId, townId);
		return new ResponseEntity<>(savedCompanyDTO, HttpStatus.CREATED);
	}

	@GetMapping("/admin/companies")
	public ResponseEntity<CompanyResponse> getAllCompanies(
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		CompanyResponse allCompaniesResponse = companyService.getAllCompanies(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allCompaniesResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/companies/{companyId}")
	public ResponseEntity<CompanyDTO> deleteCompanyType(@PathVariable Integer companyId) {

		CompanyDTO deletedCompanyDTO = companyService.deleteCompany(companyId);
		return new ResponseEntity<>(deletedCompanyDTO, HttpStatus.OK);
	}

}
