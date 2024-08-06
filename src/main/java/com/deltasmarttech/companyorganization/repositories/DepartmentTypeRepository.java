package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Integer> {

	DepartmentType findByName(String name);
}
