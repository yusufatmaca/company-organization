package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.CompanyType;
import com.deltasmarttech.companyorganization.payloads.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyTypeResponse;
import com.deltasmarttech.companyorganization.repositories.CompanyTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyTypeServiceImpl implements CompanyTypeService {

	@Autowired
	private CompanyTypeRepository companyTypeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CompanyTypeDTO createCompanyType(CompanyTypeDTO companyTypeDTO) {
		CompanyType companyType = modelMapper.map(companyTypeDTO, CompanyType.class);

		companyTypeRepository.findByName(companyTypeDTO.getName())
				.orElseThrow(() -> new APIException("Company Type with the name " + companyType.getName() + " already exists!"));

		companyType.setActive(true);
		CompanyType save = companyTypeRepository.save(companyType);
		return modelMapper.map(save, CompanyTypeDTO.class);
	}

	@Override
	public CompanyTypeResponse getAllCompanyTypes() {
		List<CompanyType> companyTypes = companyTypeRepository.findAll();
		if (companyTypes.isEmpty())
			throw new APIException("There is no company type!");

		List<CompanyTypeDTO> companyTypeDTOS = companyTypes.stream().map(companyType -> modelMapper.map(companyType, CompanyTypeDTO.class)).toList();

		CompanyTypeResponse companyTypeResponse = new CompanyTypeResponse();
		companyTypeResponse.setContent(companyTypeDTOS);

		return companyTypeResponse;
	}

	@Override
	public CompanyTypeDTO deleteCompanyType(Integer companyTypeId) {
		CompanyType companyType = companyTypeRepository.findById(companyTypeId)
				.orElseThrow(() -> new ResourceNotFoundException("CompanyType", "id", companyTypeId));

		if(companyType.isActive() == false) {
			throw new APIException(companyType.getName() + " is already passive!");
		}

		companyType.setActive(false);
		companyType.setDeletedAt(LocalDateTime.now());
		CompanyType updatedCompanyType = companyTypeRepository.save(companyType);


		CompanyTypeDTO companyTypeDTO = modelMapper.map(updatedCompanyType, CompanyTypeDTO.class);
		return companyTypeDTO;
	}
}
