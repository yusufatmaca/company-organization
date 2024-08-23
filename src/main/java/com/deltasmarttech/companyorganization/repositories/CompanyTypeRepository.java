package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {

	Optional<CompanyType> findByName(String name);
}
