package com.deltasmarttech.companyorganization.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private List<EmployeeDTO> employees;

    //private Integer pageNumber;
    //private Integer pageSize;
    //private Long totalElements;
    //private Integer totalPages;
    //private boolean lastPage;
}
