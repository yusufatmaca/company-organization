package com.deltasmarttech.companyorganization.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Town {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Town name cannot be left blank!")
	private String name;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@JsonIgnore
	@OneToMany(mappedBy = "town", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Company> companies;

	@OneToMany(mappedBy = "town", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Department> departments;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

}
