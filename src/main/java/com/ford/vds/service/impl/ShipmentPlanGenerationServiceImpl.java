package com.ford.vds.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.ShipmentPlanGenerationDao;
import com.ford.vds.model.ShipmentPlanGenerationDTO;
import com.ford.vds.model.SummaryChartPlanGenDTO;
import com.ford.vds.service.ShipmentPlanGenerationService;
import com.ford.vds.webservice.HttpUtil;
import com.ford.vds.webservice.ProcessShipmentResponse;
import com.ford.vds.webservice.ShipmentPlanRequest;

@Service
public class ShipmentPlanGenerationServiceImpl implements ShipmentPlanGenerationService {

	@Autowired
	private ShipmentPlanGenerationDao shipmentPlanDao;
	
	@Autowired
	private HttpUtil http;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentPlanGenerationServiceImpl.class);
	
	
	@Override
	public List<ShipmentPlanGenerationDTO> getDraftShipmentPlan(String authGroup) {
		
		logger.info("Inside getDraftShipmentPlan() Method: "); 
		
		List<ShipmentPlanGenerationDTO> shipmentList = shipmentPlanDao.getDraftPlan(authGroup);
		
		logger.info("Exiting getDraftShipmentPlan() Method: ");
		
		return shipmentList;
	}

	@Override
	public List<SummaryChartPlanGenDTO> getChartDetails(String chartNo,String authGroup,String screenName,String[] planNumbers) {
		
		logger.info("Inside getChartDetails() Method: ");

		List<SummaryChartPlanGenDTO> summaryChart = new ArrayList<SummaryChartPlanGenDTO>();
		if (chartNo.equals("1")) {
			
			summaryChart = shipmentPlanDao.getSummaryChart1_Details(authGroup,screenName,planNumbers);
			
		}else if(chartNo.equals("2")){
			summaryChart = shipmentPlanDao.getSummaryChart2_Details(authGroup,screenName,planNumbers);
		}
		
		logger.info("Exiting getChartDetails() Method: ");
		
		return summaryChart;
	}

	@Override
	public ProcessShipmentResponse generateShipmentPlan(String authGroup, String planNo, String userId) {
		
		logger.info("Inside generateShipmentPlan() Method: ");
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		request.setAuthorityGroup(authGroup);
		request.setShipmentNo(planNo);
		request.setUserId(userId);
		request.setProcessName(VDSConstant.DRAFT_PLAN_CONFIRM);
		try {
			
			logger.info("Generate_ShipmentPlan_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Generate_ShipmentPlan_Response:-----> "+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting generateShipmentPlan() Method: ");
		
		return response;
	}

	@Override
	public ProcessShipmentResponse cancelShipmentPlan(String authGroup, String planNo,String userId) {
		
		logger.info("Inside cancelShipmentPlan() Method: ");
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		request.setAuthorityGroup(authGroup);
		request.setShipmentNo(planNo);
		request.setUserId(userId);
		request.setProcessName(VDSConstant.DRAFT_PLAN_CANCEL);
		try {
			logger.info("Cancel_ShipmentPlan_Request:-----> "+request);
			response = http.callWebService(request);
			logger.info("Cancel_ShipmentPlan_Response:-----> "+response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting cancelShipmentPlan() Method: ");
		
		return response;
	}

	@Override
	public ProcessShipmentResponse removeLoadNumber(String authGroup, String planNo,String userId, String[] loadNumber) {
		
		logger.info("Inside removeLoadNumber() Method: ");
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		String loadNo ="";
		int i=1;
		for (String load : loadNumber) {
			if(loadNumber.length == i) {
				loadNo += load;
			}else {
				loadNo += load+",";
			}
			i++;
		}
			request.setAuthorityGroup(authGroup);
			request.setShipmentNo(planNo);
			request.setUserId(userId);
			request.setLoadNumber(loadNo);
			request.setProcessName(VDSConstant.DRAFT_PLAN_LOAD_REMOVE);
			try {
				logger.info("RemoveLoad_ShipmentPlan_Request:------> "+request);
				response = http.callWebService(request);
				logger.info("RemoveLoad_ShipmentPlan_Response:------> "+response);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.info("Exiting removeLoadNumber() Method: ");
			
		return response;
	}
	
	public ProcessShipmentResponse createDraftPlan(String authGroup, String userId, String[] planTypeList,String validation) {
		
		logger.info("Inside createDraftPlan() Method: ");
		
		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		String planType = "";
		int i=1;
		for (String type : planTypeList) {
			if(planTypeList.length == i) {
				planType += type;
			}else {
				planType += type+",";
			}
			i++;
		}
			request.setAuthorityGroup(authGroup);
			request.setPlanType(planType);
			request.setUserId(userId);
			request.setValidation(validation);
			request.setProcessName(VDSConstant.CREATE_DRAFT_PLAN);
			try {
				logger.info("Create_ShipmentPlan_Request:-----> "+request);
				response = http.callWebService(request);
				logger.info("Create_ShipmentPlan_Response:-----> "+response);

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.info("Exiting createDraftPlan() Method: ");
			
		return response;
	}
}
