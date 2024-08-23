package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.Company;
import com.deltasmarttech.companyorganization.models.Department;
import com.deltasmarttech.companyorganization.models.Town;
import jakarta.transaction.Transactional;
import jdk.jfr.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Page<Department> findByCompanyOrderByNameAsc(Company company, Pageable pageDetails);

    @Modifying
    @Transactional
    @Query("UPDATE Department d SET d.town = null WHERE d.town = :town")
    void setTownToNull(@Param("town") Town town);

    Optional<Department> findByName(String name);
}
