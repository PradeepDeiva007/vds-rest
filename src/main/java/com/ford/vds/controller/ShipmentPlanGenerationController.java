package com.ford.vds.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ford.vds.model.ShipmentPlanGenerationDTO;
import com.ford.vds.model.SummaryChartPlanGenDTO;
import com.ford.vds.service.ShipmentPlanGenerationService;
import com.ford.vds.webservice.ProcessShipmentResponse;

@Controller()
@RequestMapping("shipmentPlan/")
public class ShipmentPlanGenerationController {

	@Autowired
	private ShipmentPlanGenerationService spService;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentPlanGenerationController.class);

	@GetMapping(path = "search", params = { "authGroup" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ShipmentPlanGenerationDTO>> getShipmentPlanGenerationData(
			@RequestParam(value = "authGroup") String authGroupValue) {
		
		logger.info("Called SuccessFully getShipmentPlanGenerationData() Controller: ");
		
		return new ResponseEntity<List<ShipmentPlanGenerationDTO>>(spService.getDraftShipmentPlan(authGroupValue),
				HttpStatus.OK);
	}

	@GetMapping(path = "summaryChart/{authGrp}", params = { "chart", "screen","planList" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<SummaryChartPlanGenDTO>> getSummaryChartDetails(
			@PathVariable String authGrp, @RequestParam(value = "chart") String chartNo,
			@RequestParam(value = "screen") String screenType, @RequestParam(value = "planList") String[] planList) {
		
		logger.info("Called SuccessFully getSummaryChartDetails() Controller: ");
		
		return new ResponseEntity<List<SummaryChartPlanGenDTO>>(spService.getChartDetails(chartNo, authGrp, screenType, planList), 
				HttpStatus.OK);

	}

	@PostMapping(path = "create/{authGroup}", params = {"id","shipmentPlanNo"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> generateShipmentPlan(@PathVariable String authGroup,
			@RequestParam(value="shipmentPlanNo") String shipmentPlanNo,  @RequestParam(value="id") String userId) {
		
		logger.info("Called SuccessFully generateShipmentPlan() Controller: ");
		
		return new ResponseEntity<ProcessShipmentResponse>(spService.generateShipmentPlan(authGroup,shipmentPlanNo,userId), HttpStatus.CREATED);
	}

	@PostMapping(path = "cancel/{authGroup}", params = {"id","shipmentPlanNo"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> cancelShipmentPlan(@PathVariable String authGroup,
			@RequestParam String shipmentPlanNo, @RequestParam(value="id") String userId) {
		
		logger.info("Called SuccessFully cancelShipmentPlan() Controller: ");
		
		return new ResponseEntity <ProcessShipmentResponse>(spService.cancelShipmentPlan(authGroup,shipmentPlanNo,userId), HttpStatus.CREATED);
	}

	@PostMapping(path = "remove/{authGroup}", params = {"id","shipmentPlanNo"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> removeShipmentPlanLoadNumber(@PathVariable String authGroup,
			@RequestParam String shipmentPlanNo, @RequestParam(value="id") String userId, @RequestBody String[] loadNumber) {
		
		logger.info("Called SuccessFully removeShipmentPlanLoadNumber() Controller: ");
		
		return new ResponseEntity<ProcessShipmentResponse>(spService.removeLoadNumber(authGroup,shipmentPlanNo,userId,loadNumber),HttpStatus.CREATED);
	}
	
	@PostMapping(path ="generate/{authGroup}", params = {"id","check"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> generateDraftPlan(@PathVariable String authGroup,
			@RequestParam(value="id") String userId,@RequestParam(value="check") String validate,@RequestBody String[] planType) {
		
		logger.info("Called SuccessFully generateDraftPlan() Controller: ");
		
		return new ResponseEntity<ProcessShipmentResponse>(spService.createDraftPlan(authGroup,userId,planType,validate), HttpStatus.OK);
	}
	
	

}
