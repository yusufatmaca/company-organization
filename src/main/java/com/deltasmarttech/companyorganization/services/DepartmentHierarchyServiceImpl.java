package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.exceptions.APIException;
import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Department;
import com.deltasmarttech.companyorganization.models.DepartmentHierarchy;
import com.deltasmarttech.companyorganization.models.DepartmentHierarchyId;
import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchyDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchyResponse;
import com.deltasmarttech.companyorganization.repositories.DepartmentHierarchyRepository;
import com.deltasmarttech.companyorganization.repositories.DepartmentRepository;
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
public class DepartmentHierarchyServiceImpl implements DepartmentHierarchyService {

    @Autowired
    private DepartmentHierarchyRepository departmentHierarchyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DepartmentHierarchyDTO addDepartmentHierarchy(Integer parentDepartmentId,
                                                         Integer childDepartmentId) {

        Department parentDepartment = departmentRepository.findById(parentDepartmentId)
                .orElseThrow(() -> new APIException("Parent department not found with ID: " + parentDepartmentId));

        Department childDepartment = departmentRepository.findById(childDepartmentId)
                .orElseThrow(() -> new APIException("Child department not found with ID: " + childDepartmentId));

        if (!parentDepartment.isActive() || !childDepartment.isActive()) {
            throw new APIException("The department(s) is/are inactive!");
        }

        if (departmentHierarchyRepository.existsById(new DepartmentHierarchyId(childDepartmentId, parentDepartmentId))) {
            throw new APIException("Hierarchy already exists between the specified departments.");
        }

        DepartmentHierarchy departmentHierarchy = new DepartmentHierarchy();
        departmentHierarchy.setId(new DepartmentHierarchyId(childDepartmentId, parentDepartmentId));
        departmentHierarchy.setParentDepartment(parentDepartment);
        departmentHierarchy.setChildDepartment(childDepartment);

        departmentHierarchyRepository.save(departmentHierarchy);

        DepartmentHierarchyDTO departmentHierarchyDTO = new DepartmentHierarchyDTO();
        departmentHierarchyDTO.setParentDepartmentId(parentDepartmentId);
        departmentHierarchyDTO.setChildDepartmentId(childDepartmentId);
        departmentHierarchyDTO.setParentDepartmentName(parentDepartment.getName());
        departmentHierarchyDTO.setChildDepartmentName(childDepartment.getName());

        return departmentHierarchyDTO;
    }

    @Override
    public DepartmentHierarchyResponse getAllDepartmentHierarchyByCompany(
            Integer companyId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder) {

        Sort sort = createSort(sortBy, sortOrder);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<DepartmentHierarchy> page = departmentHierarchyRepository
                .findByParentDepartmentCompanyId(companyId, pageable);

        List<DepartmentHierarchy> departmentHierarchies = page.getContent();

        List<DepartmentHierarchyDTO> departmentHierarchyDTOS = page.getContent()
                .stream()
                .map(departmentHierarchy -> {
            DepartmentHierarchyDTO dto = new DepartmentHierarchyDTO();
            dto.setParentDepartmentId(departmentHierarchy.getParentDepartment().getId());
            dto.setChildDepartmentId(departmentHierarchy.getChildDepartment().getId());
            dto.setParentDepartmentName(departmentHierarchy.getParentDepartment().getName());
            dto.setChildDepartmentName(departmentHierarchy.getChildDepartment().getName());
            return dto;
        }).collect(Collectors.toList());

        DepartmentHierarchyResponse response = new DepartmentHierarchyResponse();
        response.setContent(departmentHierarchyDTOS);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }

    private Sort createSort(String sortBy, String sortOrder) {

        String sortField = "id";  // Default to sorting by id
        if (sortBy != null) {
            sortField = switch (sortBy.toLowerCase()) {
                case "name" -> "parentDepartment.name";
                case "childname" -> "childDepartment.name";
                case "id" -> "id";
                default -> "id";
            };
        }

        Sort.Direction direction = (sortOrder != null && sortOrder.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortField);
    }
}
