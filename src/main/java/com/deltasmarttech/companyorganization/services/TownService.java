package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.Address.Town.TownDTO;
import com.deltasmarttech.companyorganization.payloads.Address.Town.TownResponse;

public interface TownService {

	TownDTO createTown(TownDTO townDTO, Integer cityId, Integer regionId);

	TownResponse getAllTownsByRegion(Integer cityId, Integer regionId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	TownDTO deleteTown(Integer cityId, Integer regionId, Integer townId);

}
