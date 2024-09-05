package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.City;
import com.deltasmarttech.companyorganization.models.Region;
import com.deltasmarttech.companyorganization.models.Town;
import com.deltasmarttech.companyorganization.payloads.Address.City.CityDTO;
import com.deltasmarttech.companyorganization.payloads.Address.City.CityResponse;
import com.deltasmarttech.companyorganization.repositories.CityRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public CityDTO createCity(CityDTO cityDTO) {
		City city = modelMapper.map(cityDTO, City.class);
		Optional<City> savedCity = cityRepository.findByName(cityDTO.getName());

		if (savedCity.isPresent()) {
			throw new APIException("City with the name: " + cityDTO.getName() + " already exists!");
		}

		city.setCreatedAt(LocalDateTime.now());
		City c = cityRepository.save(city);
		return modelMapper.map(c, CityDTO.class);
	}

	@Override
	public CityResponse getAllCities(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<City> cityPage = cityRepository.findAll(pageDetails);

		List<City> cities = cityPage.getContent();
		if (cities.isEmpty())
			throw new APIException("There are no cities!");

		List<CityDTO> cityDTOS = cities.stream()
				.map(city -> modelMapper.map(city, CityDTO.class))
				.toList();

		CityResponse cityResponse = new CityResponse();
		cityResponse.setContent(cityDTOS);
		cityResponse.setPageNumber(cityPage.getNumber());
		cityResponse.setPageSize(cityPage.getSize());
		cityResponse.setTotalElements(cityPage.getTotalElements());
		cityResponse.setTotalPages(cityPage.getTotalPages());
		cityResponse.setLastPage(cityPage.isLast());
		return cityResponse;
	}

	@Override
	public CityDTO deleteCity(Integer cityId) {

		City city = cityRepository.findById(cityId)
				.orElseThrow(() -> new ResourceNotFoundException("City", "id", cityId));

		for (Region region : city.getRegions()) {
			for (Town town : region.getTowns()) {
				departmentRepository.setTownToNull(town);
			}
		}

		cityRepository.delete(city);
		return modelMapper.map(city, CityDTO.class);
	}


}
