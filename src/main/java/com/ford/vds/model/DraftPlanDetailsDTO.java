package com.ford.vds.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class DraftPlanDetailsDTO {
	
	private String shipmentPlanNo;
	private String vinNo;
	private String loadNo;
	private String vinCost;
	private String plannedLocation;
	private String dealer;
	private String destinationCity;
	private String model;
	private String vlsp;
	private String transportMode;

}
