package com.ford.vds.service;

import java.util.List;

import com.ford.vds.model.ShipmentCancelPlanDTO;
import com.ford.vds.webservice.ProcessShipmentResponse;

public interface ShipmentCancelService {
	List<ShipmentCancelPlanDTO> searchCancelPlan(String planNo, String authGroupValue);
	
	ProcessShipmentResponse cancelShipmentPlan(String authGroup,String planNo,String userId,String approvalcoments);
	
	List<String> loadShipmentPlan(String authGroupValue);
		
}
