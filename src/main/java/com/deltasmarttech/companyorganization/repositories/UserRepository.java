package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.AppRole;
import com.deltasmarttech.companyorganization.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Page<User> findByRole_RoleNameAndDepartment_IdNot(AppRole roleName, Integer departmentId, Pageable pageable);

    Page<User> findByRole_RoleNameAndDepartment_IdNotAndActiveTrueAndEnabledTrue(
            AppRole roleName, Integer departmentId, Pageable pageable);
}
