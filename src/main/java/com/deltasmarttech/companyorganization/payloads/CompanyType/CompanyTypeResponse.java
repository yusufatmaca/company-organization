package com.deltasmarttech.companyorganization.payloads.CompanyType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTypeResponse {

	List<CompanyTypeDTO> content;
}
