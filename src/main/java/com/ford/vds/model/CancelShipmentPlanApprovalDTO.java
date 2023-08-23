package com.ford.vds.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class CancelShipmentPlanApprovalDTO {
	
	private String shipmentPlanno;

}
