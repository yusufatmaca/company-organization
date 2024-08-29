package com.deltasmarttech.companyorganization.payloads.DepartmentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentTypeDTO {

	private Integer id;
	private String name;
	private boolean active;

}
