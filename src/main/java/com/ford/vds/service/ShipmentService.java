package com.ford.vds.service;

import java.util.List;

import com.ford.vds.model.ShipmentPlanDTO;

public interface ShipmentService {

	List<ShipmentPlanDTO> searchShipmentPlan(List<String> loadId, List<String> city, List<String> location,
			List<String> transportMode, List<String> vlsp);

	List<ShipmentPlanDTO> getSummaryChart(String chartNo);
}
