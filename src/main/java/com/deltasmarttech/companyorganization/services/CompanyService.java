package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.Company.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.Company.CompanyResponse;

public interface CompanyService {

	CompanyDTO createCompany(CompanyDTO companyDTO, Integer companyTypeId, Integer townId);
	CompanyResponse getAllCompanies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	CompanyDTO deleteCompany(Integer companyId);
    CompanyDTO updateCompany(Integer companyId, CompanyDTO companyDTO);
}
