package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

	Optional<City> findByName(String name);
}
