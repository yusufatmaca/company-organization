package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.RegionDTO;
import com.deltasmarttech.companyorganization.payloads.RegionResponse;
import com.deltasmarttech.companyorganization.services.RegionService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/api")
public class RegionController {

	@Autowired
	private RegionService regionService;

	@PostMapping("/executive/cities/{cityId}/regions")
	public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionDTO regionDTO, @PathVariable Integer cityId) {

		RegionDTO savedRegionDTO = regionService.createRegion(regionDTO, cityId);
		return new ResponseEntity<>(savedRegionDTO, HttpStatus.CREATED);
	}

	@GetMapping("/cities/{cityId}/regions")
	public ResponseEntity<RegionResponse> getAllRegionsByCity(
			@PathVariable Integer cityId,

			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,

			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		RegionResponse allRegionsByCityResponse = regionService.getAllRegionsByCity(cityId, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allRegionsByCityResponse, HttpStatus.OK);
	}

	@DeleteMapping("/executive/cities/{cityId}/regions/{regionId}")
	public ResponseEntity<RegionDTO> deleteRegion(@PathVariable Integer regionId) {

		RegionDTO deletedRegionDTO = regionService.deleteRegion(regionId);
		return new ResponseEntity<>(deletedRegionDTO, HttpStatus.OK);
	}

}
