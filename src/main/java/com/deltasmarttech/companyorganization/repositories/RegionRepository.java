package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.City;
import com.deltasmarttech.companyorganization.models.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer> {

	Optional<Region> findByName(String name);
	Page<Region> findByCityOrderByNameAsc(City city, Pageable pageDetails);
}
