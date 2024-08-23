package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.Town;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

	Page<Town> findAllByRegionId(Integer regionId, Pageable pageable);

    Optional<Town> findByName(String name);
}
