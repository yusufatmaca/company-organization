package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.CompanyType;
import com.deltasmarttech.companyorganization.models.Town;
import com.deltasmarttech.companyorganization.payloads.Address.AddressDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyResponse;
import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.Department.DepartmentDTO;
import com.deltasmarttech.companyorganization.payloads.Department.Employee.AddOrRemoveEmployeeResponse;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.repositories.CompanyRepository;
import com.deltasmarttech.companyorganization.repositories.CompanyTypeRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
import com.deltasmarttech.companyorganization.repositories.TownRepository;
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

		CompanyDTO companyDTO = modelMapper.map(company, CompanyDTO.class);
		companyDTO.setActive(company.isActive());
		List<DepartmentDTO> departmentDTOs = company.getDepartments()
				.stream()
				.map(departmentServiceImpl::converttoDepartmentDTO)
				.collect(Collectors.toList());

		return companyDTO;
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
}
