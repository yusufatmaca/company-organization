package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.models.DepartmentType;
import com.deltasmarttech.companyorganization.models.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Integer> {

    ProfilePicture findByUserId(Integer userId);
}
