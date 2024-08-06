package com.deltasmarttech.companyorganization.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Company Type name cannot be left blank!")
	private String name;

	private boolean active;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

	@JsonIgnore
	@OneToMany(mappedBy = "companyType")
	private List<Company> companies = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
