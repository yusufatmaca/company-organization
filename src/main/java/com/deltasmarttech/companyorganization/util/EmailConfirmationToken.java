package com.deltasmarttech.companyorganization.util;


import com.deltasmarttech.companyorganization.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String verificationCode;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public EmailConfirmationToken(User user, String verificationCode) {

        this.user = user;
        this.verificationCode = verificationCode;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(15);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
