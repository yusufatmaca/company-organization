package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.City;
import com.deltasmarttech.companyorganization.models.Region;
import com.deltasmarttech.companyorganization.models.Town;
import com.deltasmarttech.companyorganization.payloads.TownDTO;
import com.deltasmarttech.companyorganization.payloads.TownResponse;
import com.deltasmarttech.companyorganization.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TownServiceImpl implements TownService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private TownRepository townRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	@Override
	public TownDTO createTown(TownDTO townDTO, Integer cityId, Integer regionId) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() ->
						new ResourceNotFoundException("City", "id", cityId));

		Region region = regionRepository.findById(regionId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Region", "id", regionId));

		if (!region.getCity().getId().equals(cityId)) {
			throw new APIException("The specified region does not belong to the specified city");
		}

		boolean townExists = region.getTowns().stream()
				.anyMatch(town -> town.getName().equalsIgnoreCase(townDTO.getName()));

		if(townExists) {
			throw new APIException("Town already exists!");
		}

		Town town = new Town();
		town.setName(townDTO.getName());
		town.setRegion(region);
		town = townRepository.save(town);

		region.getTowns().add(town);
		regionRepository.save(region);

		city.getRegions().add(region);
		cityRepository.save(city);

		return modelMapper.map(town, TownDTO.class);

	}

	@Override
	public TownResponse getAllTownsByRegion(Integer cityId, Integer regionId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() -> new ResourceNotFoundException("City", "id", cityId));

		Region region = regionRepository.findById(regionId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Region", "id", regionId));

		if (!region.getCity().getId().equals(cityId)) {
			throw new APIException("The specified region does not belong to the specified city");
		}

		Sort sort = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Town> townPage = townRepository.findAllByRegionId(regionId, pageable);

		List<TownDTO> townDTOs = townPage.getContent().stream()
				.map(town -> modelMapper.map(town, TownDTO.class))
				.collect(Collectors.toList());

		TownResponse townResponse = new TownResponse();
		townResponse.setContent(townDTOs);
		townResponse.setPageNumber(townPage.getNumber());
		townResponse.setPageSize(townPage.getSize());
		townResponse.setTotalElements(townPage.getTotalElements());
		townResponse.setTotalPages(townPage.getTotalPages());
		townResponse.setLastPage(townPage.isLast());
		return townResponse;
	}

	@Override
	@Transactional
	public TownDTO deleteTown(Integer cityId, Integer regionId, Integer townId) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() -> new ResourceNotFoundException("City", "id", cityId));

		Region region = regionRepository.findById(regionId)
				.orElseThrow(() -> new ResourceNotFoundException("Region", "id", regionId));

		if (!region.getCity().getId().equals(cityId)) {
			throw new APIException("The specified region does not belong to the specified city");
		}

		Town town = townRepository.findById(townId)
				.orElseThrow(() -> new ResourceNotFoundException("Town", "id", townId));

		if (!town.getRegion().getId().equals(regionId)) {
			throw new APIException("The specified town does not belong to the specified region");
		}

		companyRepository.setTownToNull(town);
		departmentRepository.setTownToNull(town);
		townRepository.delete(town);

		return modelMapper.map(town, TownDTO.class);
	}


}
