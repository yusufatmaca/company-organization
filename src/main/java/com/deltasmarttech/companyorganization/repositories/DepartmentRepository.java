package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Department;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Page<Department> findByCompanyOrderByNameAsc(Company company, Pageable pageDetails);

}
