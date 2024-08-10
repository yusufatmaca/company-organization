package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.DepartmentHierarchy;
import com.deltasmarttech.companyorganization.models.DepartmentHierarchyId;
import com.deltasmarttech.companyorganization.payloads.RegionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentHierarchyRepository extends JpaRepository<DepartmentHierarchy, DepartmentHierarchyId> {

    Page<DepartmentHierarchy> findByParentDepartmentCompanyIdOrChildDepartmentCompanyId(
            Integer parentCompanyId,
            Integer childCompanyId,
            Pageable pageable
    );
}