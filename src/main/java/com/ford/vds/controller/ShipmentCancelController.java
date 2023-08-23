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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ford.vds.model.ShipmentCancelPlanDTO;
import com.ford.vds.service.ShipmentCancelService;
import com.ford.vds.webservice.ProcessShipmentResponse;


@Controller()
@RequestMapping("shipmentPlanCancel/")
		
public class ShipmentCancelController {
	
	@Autowired
	private ShipmentCancelService shipmentCancelService;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentCancelController.class);
	
	
	
	@GetMapping(path = "load/{authoritygroup}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> loadshipmentplan(@PathVariable String authoritygroup) {
		return new ResponseEntity<List<String>>(
				shipmentCancelService.loadShipmentPlan(authoritygroup),HttpStatus.OK);
	}

	@GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ShipmentCancelPlanDTO>> handleSearchShipment(
			@RequestParam(required = false, name = "planNo") String planNo,
			@RequestParam(required = false, name = "authGroup") String authGroupValue){
		
		logger.info("Called Successfully handleSearchShipment() Controller: ");
			
		return new ResponseEntity<List<ShipmentCancelPlanDTO>>(
				shipmentCancelService.searchCancelPlan(planNo,authGroupValue), HttpStatus.OK);
			
	}
	
	@PostMapping(path = "cancelPlan/{authGroup}", params = {"id","shipmentPlanNo","comment"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> cancelShipmentPlan(@PathVariable String authGroup,
			@RequestParam String shipmentPlanNo, @RequestParam(value="id") String userId, @RequestParam(value="comment") String approvalcoments) {
		
		logger.info("Called Successfully cancelShipmentPlan() Controller: ");
		
		return new ResponseEntity <ProcessShipmentResponse>(shipmentCancelService.cancelShipmentPlan(authGroup,shipmentPlanNo,userId,approvalcoments), HttpStatus.CREATED);
	}

}
