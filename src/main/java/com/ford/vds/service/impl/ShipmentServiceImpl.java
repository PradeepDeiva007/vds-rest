package com.ford.vds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.dao.ShipmentDao;
import com.ford.vds.model.ShipmentPlanDTO;
import com.ford.vds.service.ShipmentService;

@Service
public class ShipmentServiceImpl implements ShipmentService {

	@Autowired
	private ShipmentDao shipmentDao;

	@Override
	public List<ShipmentPlanDTO> searchShipmentPlan(List<String> loadId, List<String> city, List<String> location,
			List<String> transportMode, List<String> vlsp) {
		List<ShipmentPlanDTO> shipmentList = new ArrayList<>();
		try {
			shipmentList = shipmentDao.getShipmentPlanByParams(loadId, city, location, transportMode, vlsp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipmentList;
	}

	@Override
	public List<ShipmentPlanDTO> getSummaryChart(String chartNo) {
		return null;
	}

}
