package com.deltasmarttech.companyorganization.models;

import com.deltasmarttech.companyorganization.models.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ToString.Exclude
	@Enumerated(EnumType.STRING)
	@Column(length = 20, name = "role_name")
	private AppRole roleName;

	public Role(AppRole roleName) {
		this.roleName = roleName;
	}

	/*
	@OneToMany(mappedBy = "role")
	private List<User> users = new ArrayList<>();
	*/

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
}
