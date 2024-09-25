package com.deltasmarttech.companyorganization.controllers;

import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchy.DepartmentHierarchyDTO;
import com.deltasmarttech.companyorganization.payloads.DepartmentHierarchy.DepartmentHierarchyResponse;
import com.deltasmarttech.companyorganization.services.DepartmentHierarchyService;
import com.deltasmarttech.companyorganization.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://company-organization-software-gamma.vercel.app/", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class DepartmentHierarchyController {

    @Autowired
    private DepartmentHierarchyService departmentHierarchyService;

    @PostMapping("/admin/departments/{parentDepartmentId}/{childDepartmentId}")
    public ResponseEntity<DepartmentHierarchyDTO> addDepartmentHierarchy(
            @PathVariable Integer parentDepartmentId,
            @PathVariable Integer childDepartmentId) {

        DepartmentHierarchyDTO hierarchyDTO = departmentHierarchyService
                .addDepartmentHierarchy(parentDepartmentId, childDepartmentId);

        return new ResponseEntity<>(hierarchyDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/companies/{companyId}")
    public ResponseEntity<DepartmentHierarchyResponse> getAllDepartmentHierarchyByCompany(
            @PathVariable Integer companyId,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name="pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name="sortBy", defaultValue = "parentDepartment.name", required = false) String sortBy,
            @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        DepartmentHierarchyResponse response = departmentHierarchyService.getAllDepartmentHierarchyByCompany(
                companyId,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
