package com.deltasmarttech.companyorganization.payloads.DepartmentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentTypeResponse {

	List<DepartmentTypeDTO> content;

}
