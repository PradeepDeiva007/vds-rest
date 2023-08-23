package com.ford.vds.dao;

import java.util.List;

import com.ford.vds.model.ShipmentCancelPlanDTO;


public interface ShipmentCancelDao {
	
	public List<ShipmentCancelPlanDTO> getCancelPlanByParams(String planNo, String authGroupValue) throws Exception;
	public List<String> loadCancelPlanNo(String authGroupValue) throws Exception;
}
