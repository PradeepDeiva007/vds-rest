package com.ford.vds.dao;

import java.util.List;

public interface CancelShipmentPlanApprovalDao {
	
	public List<String> getShipmentPlan(String id,String userId) throws Exception;


}
