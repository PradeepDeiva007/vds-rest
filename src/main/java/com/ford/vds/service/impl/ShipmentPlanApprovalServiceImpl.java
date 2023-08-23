package com.ford.vds.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.ShipmentPlanApprovalDao;
import com.ford.vds.service.ShipmentPlanApprovalService;
import com.ford.vds.webservice.HttpUtil;
import com.ford.vds.webservice.ProcessShipmentResponse;
import com.ford.vds.webservice.ShipmentPlanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ShipmentPlanApprovalServiceImpl implements ShipmentPlanApprovalService {

	@Autowired
	private ShipmentPlanApprovalDao shipmentPlanApprovalDao;
	
	@Autowired
	private HttpUtil http;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentPlanApprovalServiceImpl.class);
	
	@Override
	public List<String> loadShipmentPlanapprovalwaiting(String authoritygroup,String userId) {
		
		logger.info("Inside loadShipmentPlanapprovalwaiting() Method: "); 
		
		List<String> shipmentplanoList = new ArrayList<String>();
		try {
			
			shipmentplanoList = shipmentPlanApprovalDao.getShipmentPlan(authoritygroup,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting loadShipmentPlanapprovalwaiting() Method: ");
		
		return shipmentplanoList;
	}

	@Override
	public ProcessShipmentResponse shipmentapprove(String authGrp,String userId,String[] shipmentplanno,String approvalcmt) {
		
		logger.info("Inside shipmentapprove() Method: "); 
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		StringBuilder planno = new StringBuilder();
		
		for(String Spno:shipmentplanno)
		{
			planno.append(Spno).append(",");
		}
		if(planno.length()>0)
		{
			planno.deleteCharAt(planno.length()-1);
		}	
	    request.setAuthorityGroup(authGrp);
	    request.setShipmentNo(planno.toString());
	    request.setUserId(userId);
	    request.setProcessName(VDSConstant.SHIPMENTPLAN_APPROVAL);
	    request.setApprovalcmt(approvalcmt);
	    
	    try {
	    	logger.info("Generate_ShipmentPlanapprove_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Generate_ShipmentPlanapprove_Response:-----> "+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	    logger.info("Exiting shipmentapprove() Method: ");
		return response;
	}

	@Override
	public ProcessShipmentResponse shipmentreject(String authGrp,String userId,String[] shipmentplanno,String approvalcmt) {
		logger.info("Inside shipmentreject() Method: "); 
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		StringBuilder planno = new StringBuilder();
		
		for(String Spno:shipmentplanno)
		{
			planno.append(Spno).append(",");
		}
		if(planno.length()>0)
		{
			planno.deleteCharAt(planno.length()-1);
		}	
	    request.setAuthorityGroup(authGrp);
	    request.setShipmentNo(planno.toString());
	    request.setUserId(userId);
	    request.setProcessName(VDSConstant.SHIPMENTPLAN_REJECT);
	    request.setApprovalcmt(approvalcmt);
	    
	    try {
	    	logger.info("Generate_ShipmentPlanreject_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Generate_ShipmentPlanreject_Response:-----> "+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    logger.info("Exiting shipmentreject() Method: ");
	
		return response;
	}	
}
