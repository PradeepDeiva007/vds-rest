package com.ford.vds.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.ShipmentCancelDao;

import com.ford.vds.model.ShipmentCancelPlanDTO;
import com.ford.vds.service.ShipmentCancelService;
import com.ford.vds.webservice.HttpUtil;
import com.ford.vds.webservice.ProcessShipmentResponse;
import com.ford.vds.webservice.ShipmentPlanRequest;

@Service
public class ShipmentCancelServiceImpl implements ShipmentCancelService {
	@Autowired
	private ShipmentCancelDao cancelDao;
	
	@Autowired
	private HttpUtil http;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentCancelServiceImpl.class);

	public List<ShipmentCancelPlanDTO> searchCancelPlan(String planNo,String authGroupValue) {
		
		logger.info("Inside searchCancelPlan() Method: ");
		
		List<ShipmentCancelPlanDTO> shipmentList = new ArrayList<>();
		try {
			shipmentList = cancelDao.getCancelPlanByParams(planNo,authGroupValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting searchCancelPlan() Method: ");
		
		return shipmentList;
	}
	
	@Override
	public ProcessShipmentResponse cancelShipmentPlan(String authGroup, String planNo,String userId, String approvalcoments) {
		
		logger.info("Inside cancelShipmentPlan() Method: ");
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		request.setAuthorityGroup(authGroup);
		request.setShipmentNo(planNo);
		request.setUserId(userId);
		request.setApprovalcmt(approvalcoments);
		request.setProcessName(VDSConstant.SHIPMENTPLAN_CANCEL);
		try {
			logger.info("CancelShipmentPlan_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("CancelShipmentPlan_Response:-----> "+response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting cancelShipmentPlan() Method: ");
		
		return response;
	}

	@Override
	public List<String> loadShipmentPlan(String authGroupValue) {
		List<String> shipmentList  = new ArrayList<>();
		try {
			shipmentList = cancelDao.loadCancelPlanNo(authGroupValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return shipmentList;
	}
}
