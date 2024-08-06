package com.deltasmarttech.companyorganization.models;

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

	private Integer childDepartmentId;
	private Integer parentDepartmentId;

}
