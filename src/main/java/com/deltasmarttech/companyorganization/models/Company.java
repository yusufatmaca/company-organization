package com.deltasmarttech.companyorganization.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Company name cannot be left blank!")
	private String name;
	private String shortName;

	@ManyToOne
	@JoinColumn(name = "company_type_id")
	private CompanyType companyType;

	@ManyToOne
	@JoinColumn(name = "town_id", nullable = true)
	private Town town;

	private String addressDetail;

	private boolean active = true;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "company")
	private List<Department> departments;
}
