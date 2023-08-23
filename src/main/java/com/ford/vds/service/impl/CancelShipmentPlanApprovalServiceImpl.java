package com.ford.vds.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.CancelShipmentPlanApprovalDao;
import com.ford.vds.service.CancelShipmentPlanApprovalService;
import com.ford.vds.webservice.HttpUtil;
import com.ford.vds.webservice.ProcessShipmentResponse;
import com.ford.vds.webservice.ShipmentPlanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CancelShipmentPlanApprovalServiceImpl  implements  CancelShipmentPlanApprovalService{

	
	@Autowired
	private CancelShipmentPlanApprovalDao  cancelShipmentPlanApprovalDao;
	
	@Autowired
	private HttpUtil http;
	
	private Logger logger = LoggerFactory.getLogger(CancelShipmentPlanApprovalServiceImpl.class);
	
	
	@Override
	public List<String> loadShipmentPlanapprovalwaiting(String authGrp,String userId) {
		
		logger.info("Inside loadcancelShipmentPlanapprovalwaiting() Method: "); 
		
		List<String> shipmentplanoList = new ArrayList<String>();
		try {
			shipmentplanoList = cancelShipmentPlanApprovalDao.getShipmentPlan(authGrp,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Exit loadcancelShipmentPlanapprovalwaiting() Method: ");
		return shipmentplanoList;
	}


	@Override
	public ProcessShipmentResponse shipmentapprove(String authGrp,String userId,String[] shipmentplanno,String approvalcmt) {
		
		logger.info("Inside cancelShipmentPlanapprove() Method: ");
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
	    request.setProcessName(VDSConstant.CANCELSHIPMENTPLAN_APPROVAL);
	    request.setApprovalcmt(approvalcmt);
	    
	    try {
	    	logger.info("Generate_cancelShipmentPlanapprove_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Generate_cancelShipmentPlanapprove_Response:-----> "+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	    logger.info("Exit cancelShipmentPlanapprove() Method: ");
		return response;
	}


	@Override
	public ProcessShipmentResponse shipmentreject(String authGrp,String userId,String[] shipmentplanno,String approvalcmt) {
		
		logger.info("Inside cancelshipmentplanreject: ");
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
	    request.setProcessName(VDSConstant.CANCELSHIPMENTPLAN_REJECT);
	    request.setApprovalcmt(approvalcmt);
	    
	    try {
	    	logger.info("Generate_cancelShipmentPlanreject_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Generate_cancelShipmentPlanapprove_Response:-----> "+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    logger.info("Exit cancelshipmentplanreject: ");
		return response;
	}


	@Override
	public Boolean sendNotifications(String shipmentPlanNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
