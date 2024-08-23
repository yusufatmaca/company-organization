package com.deltasmarttech.companyorganization.models;

import com.deltasmarttech.companyorganization.models.City;
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
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Region name cannot be left blank!")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id", nullable = false)
	private City city;

	/*
	A region can have multiple towns,
	But, a town belongs only to a region.
	One-to-Many relationship from <<Region>> to <<Town>>
	*/
	@OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
	/*
	`Region` shows Towns, but `Town` has `Region` field therefore it shows `Region`, too. Circular reference occurs... (Region -> Town -> Region)
	Let's block it! (Region -> Town -!> Region)
	*/
	private List<Town> towns;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public Region(String name) {
		this.name = name;
	}
}
