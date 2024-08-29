package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.City;
import com.deltasmarttech.companyorganization.models.Region;
import com.deltasmarttech.companyorganization.models.Town;
import com.deltasmarttech.companyorganization.payloads.Address.Region.RegionDTO;
import com.deltasmarttech.companyorganization.payloads.Address.Region.RegionResponse;
import com.deltasmarttech.companyorganization.repositories.CityRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
import com.deltasmarttech.companyorganization.repositories.RegionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public RegionDTO createRegion(RegionDTO regionDTO, Integer cityId) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() ->
						new ResourceNotFoundException("City", "id", cityId));

		boolean isRegionNotPresent = city.getRegions().stream()
				.noneMatch(region -> region.getName().equalsIgnoreCase(regionDTO.getName()));

		if(isRegionNotPresent) {
			Region region = new Region();
			region.setName(regionDTO.getName());
			region.setCity(city);
			region = regionRepository.save(region);

			city.getRegions().add(region);
			cityRepository.save(city);

			return modelMapper.map(region, RegionDTO.class);
		} else {
			throw new APIException("Region already exists!");
		}
	}

	@Override
	public RegionResponse getAllRegionsByCity(Integer cityId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() ->
						new ResourceNotFoundException("City", "id", cityId));

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Region> pageRegions = regionRepository.findByCityOrderByNameAsc(city, pageDetails);

		List<Region> regions = pageRegions.getContent();

		if(regions.isEmpty()){
			throw new APIException(city.getName() + " city does not have any regions!");
		}

		List<RegionDTO> regionDTOS = regions.stream()
				.map(region -> modelMapper.map(region, RegionDTO.class))
				.toList();

		RegionResponse regionResponse = new RegionResponse();
		regionResponse.setContent(regionDTOS);
		regionResponse.setPageNumber(pageRegions.getNumber());
		regionResponse.setPageSize(pageRegions.getSize());
		regionResponse.setTotalElements(pageRegions.getTotalElements());
		regionResponse.setTotalPages(pageRegions.getTotalPages());
		regionResponse.setLastPage(pageRegions.isLast());
		return regionResponse;
	}

	@Override
	public RegionDTO deleteRegion(Integer regionId) {

		Region region = regionRepository.findById(regionId)
				.orElseThrow(() -> new ResourceNotFoundException("Region", "id", regionId));

		for (Town town : region.getTowns()) {
			departmentRepository.setTownToNull(town);
		}

		regionRepository.delete(region);
		return modelMapper.map(region, RegionDTO.class);
	}


}
