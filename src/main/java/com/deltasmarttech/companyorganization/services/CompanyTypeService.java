package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyType.CompanyTypeResponse;

public interface CompanyTypeService {

	CompanyTypeDTO createCompanyType(CompanyTypeDTO companyTypeDTO);
	CompanyTypeResponse getAllCompanyTypes();
	CompanyTypeDTO deleteCompanyType(Integer companyTypeId);
}
