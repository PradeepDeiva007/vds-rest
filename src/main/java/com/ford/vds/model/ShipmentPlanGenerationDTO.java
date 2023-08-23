package com.ford.vds.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class ShipmentPlanGenerationDTO {
	
	private String loadNo;
	private String city;
    private String quantity;
    private String plannedLocation;
    private String vlsp;
    private String transportMode;
    private String shipmentPlanNo;
    private String generatedBy;
    private String totalDeliveryCost;
    private String totalVins;
    private List<DraftPlanDetailsDTO> exportDraftPlan;
    

}
