package com.deltasmarttech.companyorganization.repositories;

import com.deltasmarttech.companyorganization.util.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Integer> {

    Optional<EmailConfirmationToken> findByVerificationCode(String verificationCode);
    Optional<EmailConfirmationToken> findByUserId(Integer userId);
    EmailConfirmationToken deleteByVerificationCode(String verificationCode);

}
