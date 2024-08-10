package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHierarchyDTO {

	private Integer parentDepartmentId;
	private Integer childDepartmentId;
	private String parentDepartmentName;
	private String childDepartmentName;

}
