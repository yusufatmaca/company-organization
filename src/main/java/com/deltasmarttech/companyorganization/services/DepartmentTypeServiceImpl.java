package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.exceptions.ResourceNotFoundException;
import com.deltasmarttech.companyorganization.models.DepartmentType;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentType.DepartmentTypeResponse;
import com.deltasmarttech.companyorganization.repositories.DepartmentTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepartmentTypeServiceImpl implements DepartmentTypeService {

	@Autowired
	private DepartmentTypeRepository departmentTypeRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public DepartmentTypeDTO createDepartmentType(DepartmentTypeDTO departmentTypeDTO) {

		DepartmentType departmentType = modelMapper.map(departmentTypeDTO, DepartmentType.class);
		DepartmentType savedDepartmentType = departmentTypeRepository.findByName(departmentTypeDTO.getName())
				.orElseThrow(() -> new APIException("Department type with the name " + departmentType.getName() + " already exists!"));

		departmentType.setActive(true);
		DepartmentType save = departmentTypeRepository.save(departmentType);
		return modelMapper.map(save, DepartmentTypeDTO.class);
	}

	@Override
	public DepartmentTypeResponse getAllDepartmentTypes() {

		List<DepartmentType> departmentTypes = departmentTypeRepository.findAll();
		if (departmentTypes.isEmpty())
			throw new APIException("There is no department type!");

		List<DepartmentTypeDTO> departmentTypeDTOS = departmentTypes.stream().map(departmentType -> modelMapper.map(departmentType, DepartmentTypeDTO.class)).toList();

		DepartmentTypeResponse departmentTypeResponse = new DepartmentTypeResponse();
		departmentTypeResponse.setContent(departmentTypeDTOS);

		return departmentTypeResponse;
	}

	@Override
	public DepartmentTypeDTO deleteDepartmentType(Integer departmentTypeId) {
		DepartmentType departmentType = departmentTypeRepository.findById(departmentTypeId)
				.orElseThrow(() -> new ResourceNotFoundException("Department type", "id", departmentTypeId));

		if(departmentType.isActive() == false) {
			throw new APIException(departmentType.getName() + " is already passive!");
		}

		departmentType.setActive(false);
		departmentType.setDeletedAt(LocalDateTime.now());
		DepartmentType deletedDepartmentType = departmentTypeRepository.save(departmentType);


		return modelMapper.map(deletedDepartmentType, DepartmentTypeDTO.class);

	}
}
