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

	@ManyToOne
	@JoinColumn(name = "town_id")
	private Town town;

	private String addressDetail;

	private boolean active = true;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private User manager;

}
