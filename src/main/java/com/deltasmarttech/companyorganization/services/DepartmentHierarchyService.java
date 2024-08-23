package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchyDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchyResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHierarchyService {

    DepartmentHierarchyDTO addDepartmentHierarchy(Integer parentDepartmentId, Integer childDepartmentId);
    DepartmentHierarchyResponse getAllDepartmentHierarchyByCompany(
            Integer companyId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder);
}
