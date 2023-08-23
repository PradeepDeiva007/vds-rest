package com.ford.vds.dao;

import java.util.List;

import com.ford.vds.model.ShipmentPlanDTO;

public interface ShipmentDao {

	public List<ShipmentPlanDTO> getShipmentPlanByParams(List<String> loadId, List<String> city, List<String> location,
			List<String> transportMode, List<String> vlsp) throws Exception;
}
