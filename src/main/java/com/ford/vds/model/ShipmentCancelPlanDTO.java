package com.ford.vds.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class ShipmentCancelPlanDTO {
	private String loadNo;

	private String city;

	private String plannedLocation;

	private String transportMode;

	private String vlsp;

	private String shipmentNo;
	
	private String quantity;
	
	private String planType;

}
