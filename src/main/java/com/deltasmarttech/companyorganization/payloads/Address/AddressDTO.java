package com.deltasmarttech.companyorganization.payloads.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private Integer id;
    private String cityName;
    private String regionName;
    private String townName;
}
