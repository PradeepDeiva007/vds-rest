package com.ford.vds.webservice;

import lombok.Data;

@Data
public class ShipmentPlanRequest {
	
	private String processName;

	private String shipmentNo;
	
	private String authorityGroup;

	private String planType;
	
	private String loadNumber;
	
	private String userId;
	
	private String validation;
	 
	private String approvalcmt;

}
