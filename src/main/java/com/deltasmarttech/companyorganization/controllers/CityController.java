package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.CityDTO;
import com.deltasmarttech.companyorganization.payloads.CityResponse;
import com.deltasmarttech.companyorganization.services.CityService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class CityController {

	@Autowired
	private CityService cityService;

	@PostMapping("/admin/cities")
	public ResponseEntity<CityDTO> createCity(@Valid @RequestBody CityDTO cityDTO) {

		CityDTO createdCityDTO = cityService.createCity(cityDTO);
		return new ResponseEntity<>(createdCityDTO, HttpStatus.CREATED);
	}

	@GetMapping("/public/cities")
	public ResponseEntity<CityResponse> getAllCities(
			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,
			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		CityResponse allCitiesResponse = cityService.getAllCities(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allCitiesResponse, HttpStatus.OK);
	}

	@DeleteMapping("/admin/cities/{cityId}")
	public ResponseEntity<CityDTO> deleteCity(@PathVariable Integer cityId) {

		CityDTO deletedCityDTO = cityService.deleteCity(cityId);
		return new ResponseEntity<>(deletedCityDTO, HttpStatus.OK);
	}

}