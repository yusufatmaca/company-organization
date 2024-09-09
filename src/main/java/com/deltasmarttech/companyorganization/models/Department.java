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
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Department Type name cannot be left blank!")
	private String name;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "department_type_id")
	private DepartmentType departmentType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "town_id", nullable = true)
	private Town town;

	private String addressDetail;

	private boolean active;

	public Department(String name, Company company, DepartmentType departmentType, Town town, String addressDetail, boolean active) {

		this.name = name;
		this.company = company;
		this.departmentType = departmentType;
		this.town = town;
		this.addressDetail = addressDetail;
		this.active = active;
	}

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<User> employees;

	@OneToOne
	@JoinColumn(name = "manager_id")
	private User manager;
}
