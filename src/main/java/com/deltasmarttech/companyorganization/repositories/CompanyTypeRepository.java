package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {

	CompanyType findByName(String name);
}
