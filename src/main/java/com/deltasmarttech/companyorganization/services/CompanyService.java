package com.deltasmarttech.companyorganization.services;

import com.deltasmarttech.companyorganization.payloads.CompanyDTO;
import com.deltasmarttech.companyorganization.payloads.CompanyResponse;

public interface CompanyService {

	CompanyDTO createCompany(CompanyDTO companyDTO, Integer companyTypeId, Integer townId);

	CompanyResponse getAllCompanies(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CompanyDTO deleteCompany(Integer companyId);
}
