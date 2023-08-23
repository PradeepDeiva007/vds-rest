package com.ford.vds.service;

import java.util.List;

import com.ford.vds.model.ShipmentPlanGenerationDTO;
import com.ford.vds.model.SummaryChartPlanGenDTO;
import com.ford.vds.webservice.ProcessShipmentResponse;

public interface ShipmentPlanGenerationService {

	List<ShipmentPlanGenerationDTO> getDraftShipmentPlan(String authGroup);

	List<SummaryChartPlanGenDTO> getChartDetails(String chartNo, String authGrp, String screen, String[] planNumbers);

	ProcessShipmentResponse generateShipmentPlan(String authGroup, String planNo, String userId);

	ProcessShipmentResponse cancelShipmentPlan(String authGroup, String planNo, String userId);

	ProcessShipmentResponse removeLoadNumber(String authGroup, String planNo, String userId, String[] loadNumber);

	ProcessShipmentResponse createDraftPlan(String authGroup, String userId, String[] planType, String validate);

}
