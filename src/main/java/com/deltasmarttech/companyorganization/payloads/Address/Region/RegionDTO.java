package com.deltasmarttech.companyorganization.payloads.Address.Region;

import com.deltasmarttech.companyorganization.payloads.Address.Town.TownDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {

	private Integer id;
	private String name;
	private List<TownDTO> towns;

}
