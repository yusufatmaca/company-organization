package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.TownDTO;
import com.deltasmarttech.companyorganization.payloads.TownResponse;
import com.deltasmarttech.companyorganization.services.TownService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TownController {

	@Autowired
	private TownService townService;

	@PostMapping("/executive/cities/{cityId}/regions/{regionId}/towns")
	public ResponseEntity<TownDTO> createTown(@Valid @RequestBody TownDTO townDTO, @PathVariable Integer cityId, @PathVariable Integer regionId) {

		TownDTO savedTownDTO = townService.createTown(townDTO, cityId, regionId);
		return new ResponseEntity<>(savedTownDTO, HttpStatus.CREATED);
	}

	@GetMapping("/cities/{cityId}/regions/{regionId}/towns")
	public ResponseEntity<TownResponse> getAllTownsByRegion(
			@PathVariable Integer cityId,

			@PathVariable Integer regionId,

			@RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

			@RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

			@RequestParam(name="sortBy", defaultValue = AppConstants.SORT_BY_NAME, required = false) String sortBy,

			@RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

		TownResponse allTownsByRegionResponse = townService.getAllTownsByRegion(cityId, regionId, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(allTownsByRegionResponse, HttpStatus.OK);
	}

	@DeleteMapping("/executive/cities/{cityId}/regions/{regionId}/towns/{townId}")
	public ResponseEntity<TownDTO> deleteTown(@PathVariable Integer cityId, @PathVariable Integer regionId, @PathVariable Integer townId) {

		TownDTO deletedTownDTO = townService.deleteTown(cityId, regionId, townId);
		return new ResponseEntity<>(deletedTownDTO, HttpStatus.OK);
	}

}