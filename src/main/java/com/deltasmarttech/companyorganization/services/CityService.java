package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.Address.City.CityDTO;
import com.deltasmarttech.companyorganization.payloads.Address.City.CityResponse;

public interface CityService {

	CityDTO createCity(CityDTO cityDTO);

	CityResponse getAllCities(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CityDTO deleteCity(Integer cityId);
}
