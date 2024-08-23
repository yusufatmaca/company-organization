package com.deltasmarttech.companyorganization.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHierarchy {

	@EmbeddedId
	private DepartmentHierarchyId id;

	@ManyToOne
	@MapsId("childDepartmentId")
	@JoinColumn(name = "child_department_id")
	private Department childDepartment;

	@ManyToOne
	@MapsId("parentDepartmentId")
	@JoinColumn(name = "parent_department_id")
	private Department parentDepartment;

	public DepartmentHierarchy(Department parentDepartment, Department childDepartment) {
		this.parentDepartment = parentDepartment;
		this.childDepartment = childDepartment;
		id = new DepartmentHierarchyId();
	}
}
