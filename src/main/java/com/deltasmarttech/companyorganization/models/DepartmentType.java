package com.deltasmarttech.companyorganization.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Department Type name cannot be left blank!")
	private String name;

	private boolean active;

	public DepartmentType(String name, boolean active) {
		this.name = name;
		this.active = active;
	}

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "departmentType")
	private List<Department> departments = new ArrayList<>();
}
