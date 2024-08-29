package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.Department;
import com.deltasmarttech.companyorganization.models.DepartmentHierarchy;
import com.deltasmarttech.companyorganization.models.DepartmentHierarchyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentHierarchyRepository extends JpaRepository<DepartmentHierarchy, DepartmentHierarchyId> {

    Optional<DepartmentHierarchy> findByParentDepartmentAndChildDepartment(Department parentDepartment, Department childDepartment);

    Page<DepartmentHierarchy> findByParentDepartmentCompanyId(Integer companyId, Pageable pageable);
}