package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.*;
import com.deltasmarttech.companyorganization.payloads.Address.AddressDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyResponse;
import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyTypeRepository companyTypeRepository;

	@Autowired
	private TownRepository townRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private DepartmentServiceImpl departmentServiceImpl;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Override
	public CompanyDTO createCompany(CompanyDTO companyDTO, Integer companyTypeId, Integer townId) {

		CompanyType companyType = companyTypeRepository.findById(companyTypeId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Company type", "id", companyTypeId));

		if(companyType.isActive() == false) {
			throw new APIException(companyType.getName() + " is passive!");
		}

			Town town = townRepository.findById(townId)
				.orElseThrow(() ->
						new ResourceNotFoundException("Town", "id", townId));

		Company company = modelMapper.map(companyDTO, Company.class);
		Company savedCompany = companyRepository.findByName(companyDTO.getName())
				.orElseThrow(() -> new APIException("Company with the name \"" + companyDTO.getName() + "\" already exists!"));

		company.setCompanyType(companyType);
		company.setTown(town);

		Company c = companyRepository.save(company);

		town.getCompanies().add(c);
		townRepository.save(town);

		CompanyDTO savedCompanyDTO = modelMapper.map(c, CompanyDTO.class);
		savedCompanyDTO.setCompanyType(modelMapper.map(company.getCompanyType(), CompanyTypeDTO.class));

		return savedCompanyDTO;
	}

	@Override
	public CompanyResponse getAllCompanies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Company> companyPage = companyRepository.findAll(pageDetails);

		List<Company> companies = companyPage.getContent();
		if (companies.isEmpty())
			throw new APIException("There are no companies!");


		List<CompanyDTO> companyDTOS = companies.stream()
				.map(this::convertToCompanyDTO)
				.toList();

		CompanyResponse companyResponse = new CompanyResponse();
		companyResponse.setContent(companyDTOS);
		companyResponse.setPageNumber(companyPage.getNumber());
		companyResponse.setPageSize(companyPage.getSize());
		companyResponse.setTotalElements(companyPage.getTotalElements());
		companyResponse.setTotalPages(companyPage.getTotalPages());
		companyResponse.setLastPage(companyPage.isLast());
		return companyResponse;
	}


	private CompanyDTO convertToCompanyDTO(Company company) {
		if (company == null) {
			return null; // Handle null case if required
		}

		// Convert CompanyType entity to CompanyTypeDTO
		CompanyTypeDTO companyTypeDTO = CompanyTypeDTO.builder()
				.id(company.getCompanyType().getId())
				.name(company.getCompanyType().getName())
				.active(company.getCompanyType().isActive())
				.build();

		// Extract city, region, and town names from the Town entity
		String cityName = null;
		String regionName = null;
		String townName = null;
		if (company.getTown() != null) {
			townName = company.getTown().getName();
			if (company.getTown().getRegion() != null) {
				regionName = company.getTown().getRegion().getName();
				if (company.getTown().getRegion().getCity() != null) {
					cityName = company.getTown().getRegion().getCity().getName();
				}
			}
		}

		// Build the CompanyDTO object
		return CompanyDTO.builder()
				.id(company.getId())
				.name(company.getName())
				.shortName(company.getShortName())
				.city(cityName)
				.region(regionName)
				.town(townName)
				.companyType(companyTypeDTO)
				.addressDetail(company.getAddressDetail())
				.active(company.isActive())
				.build();
	}


	@Override
	public CompanyDTO deleteCompany(Integer companyId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		if(!company.isActive()) {
			throw new APIException(company.getName() + " is already passive!");
		}

		company.setActive(false);
		company.setDeletedAt(LocalDateTime.now());
		company.getDepartments().stream()
				.forEach(department -> {
					department.setActive(false);
					department.setDeletedAt(LocalDateTime.now());
					departmentRepository.save(department);
				});
		Company updatedCompany = companyRepository.save(company);

		CompanyDTO companyDTO = convertToCompanyDTO(updatedCompany);
		return companyDTO;
	}

	@Override
	public CompanyDTO updateCompany(Integer companyId, CompanyDTO companyDTO) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company", "id", companyId));

		if (!company.isActive()) {
			throw new APIException(" You cannot edit " + company.getName() + " because it's not active!");
		}

		if (companyDTO.getName() != null && !companyDTO.getName().isBlank()) {
			company.setName(companyDTO.getName());
		}
		if (companyDTO.getShortName() != null && !companyDTO.getShortName().isBlank()) {
			company.setShortName(companyDTO.getShortName());
		}
		if (companyDTO.getCompanyType() != null && companyDTO.getCompanyType().isActive()) {
			CompanyType companyType = companyTypeRepository.findById(companyDTO.getCompanyType().getId())
					.orElseThrow(() -> new ResourceNotFoundException("CompanyType", "id", companyDTO.getCompanyType().getId()));
			company.setCompanyType(companyType);
		}
		if (companyDTO.getAddressDetail() != null && !companyDTO.getAddressDetail().isBlank()) {
			company.setAddressDetail(companyDTO.getAddressDetail());
		}
		if (companyDTO.getTown() != null && !companyDTO.getTown().isBlank()) {
			Town town = townRepository.findByName(companyDTO.getTown())
							.orElseThrow(() -> new ResourceNotFoundException("Town", "name", companyDTO.getTown()));
			company.setTown(town);
		}

		Company updatedCompany = companyRepository.save(company);
		return mapToCompanyDTO(updatedCompany);

	}

	private CompanyDTO mapToCompanyDTO(Company company) {
		return CompanyDTO.builder()
				.id(company.getId())
				.name(company.getName())
				.shortName(company.getShortName())
				.companyType(mapToCompanyTypeDTO(company.getCompanyType()))
				.addressDetail(company.getAddressDetail())
				.active(company.isActive())
				.town(company.getTown().getName())
				.region(company.getTown().getRegion().getName())
				.city(company.getTown().getRegion().getCity().getName())
				.build();
	}

	private CompanyTypeDTO mapToCompanyTypeDTO(CompanyType companyType) {
		if (companyType == null) {
			return null;
		}
		return CompanyTypeDTO.builder()
				.id(companyType.getId())
				.name(companyType.getName())
				.active(companyType.isActive())
				.build();
	}
}
