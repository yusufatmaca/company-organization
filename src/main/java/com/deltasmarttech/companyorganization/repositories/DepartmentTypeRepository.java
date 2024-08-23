package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Integer> {

	Optional<DepartmentType> findByName(String name);
}
