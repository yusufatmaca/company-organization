package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.CompanyTypeDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyTypeResponse;

public interface CompanyTypeService {

	CompanyTypeDTO createCompanyType(CompanyTypeDTO companyTypeDTO);
	CompanyTypeResponse getAllCompanyTypes();
	CompanyTypeDTO deleteCompanyType(Integer companyTypeId);
}
