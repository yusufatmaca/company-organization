package com.deltasmarttech.companyorganization.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProfilePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String type;
    private String filePath;

    @Lob
    @Column(length = 1000)
    private byte[] profilePicture;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
