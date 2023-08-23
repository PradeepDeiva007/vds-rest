package com.ford.vds.service;

import java.util.List;

import com.ford.vds.webservice.ProcessShipmentResponse;

public interface CancelShipmentPlanApprovalService {
	
	List<String> loadShipmentPlanapprovalwaiting(String authGrp,String userId);

	ProcessShipmentResponse shipmentapprove(String authGrp,String userId,String[] shipmentplanno,String approvecmt);

	ProcessShipmentResponse shipmentreject(String authGrp,String userId,String[] shipmentplanno,String approvecmt);
	
	 Boolean sendNotifications(String shipmentPlanNo);
}
