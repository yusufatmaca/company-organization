package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.RegionDTO;
import com.deltasmarttech.companyorganization.payloads.RegionResponse;

public interface RegionService {

	RegionDTO createRegion(RegionDTO regionDTO, Integer cityId);

	RegionResponse getAllRegionsByCity(Integer cityId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	RegionDTO deleteRegion(Integer regionId);
}
