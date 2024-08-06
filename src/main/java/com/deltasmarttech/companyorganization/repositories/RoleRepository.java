package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.AppRole;
import com.deltasmarttech.companyorganization.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByRoleName(AppRole appRole);
}
