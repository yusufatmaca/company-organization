package com.deltasmarttech.companyorganization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHierarchyId implements Serializable {

	@Column(name = "child_department_id")
	private Integer childDepartmentId;
	@Column(name = "parent_department_id")
	private Integer parentDepartmentId;

}
