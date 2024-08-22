package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Town;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	Company findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Company c SET c.town = null WHERE c.town = :town")
    void setTownToNull(@Param("town") Town town);
}
