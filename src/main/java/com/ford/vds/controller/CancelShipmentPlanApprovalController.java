package com.ford.vds.controller;

import java.util.List;

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

import com.ford.vds.service.CancelShipmentPlanApprovalService;
import com.ford.vds.webservice.ProcessShipmentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("cancelshipmentapproval/")
public class CancelShipmentPlanApprovalController {
	
	private Logger logger = LoggerFactory.getLogger(CancelShipmentPlanApprovalController.class);
	
	@Autowired
	private CancelShipmentPlanApprovalService  cancelShipmentPlanApprovalService;
	
	
	@GetMapping(path = "cancelsearch/{authoritygroup}",params = {"user"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> handleSearchShipment(@PathVariable String authoritygroup,@RequestParam(value="user") String userId ) {
		
		logger.info("Called SuccessFully loadcancelShipmentPlanapprovalwaiting() Controller: ");
		
		return new ResponseEntity<List<String>>(
				cancelShipmentPlanApprovalService.loadShipmentPlanapprovalwaiting(authoritygroup,userId), HttpStatus.OK);
	}
	@PostMapping(path = "cancelapprove/{authoritygroup}", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> approveshipmentplan(@PathVariable String authoritygroup,
			@RequestParam(value="id") String userId, @RequestBody String[] ShipmentPlanNumber,@RequestParam(value="comment") String approvalcoments) {
		
		logger.info("Called SuccessFully cancelShipmentPlanapprove() Controller: ");
		
		return new ResponseEntity<ProcessShipmentResponse>(cancelShipmentPlanApprovalService.shipmentapprove(authoritygroup,userId,ShipmentPlanNumber,approvalcoments),HttpStatus.CREATED);
	}
	
	
	@PostMapping(path = "cancelreject/{authoritygroup}",params= {"id"} ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> rejectShipmentplan(@PathVariable String authoritygroup,
			@RequestParam(value="id") String userId, @RequestBody String[] ShipmentPlanNumber,@RequestParam(value="comment") String approvalcoments) {
		
		logger.info("Called SuccessFully cancelShipmentPlanreject() Controller: ");
		
		return new ResponseEntity<ProcessShipmentResponse>(cancelShipmentPlanApprovalService.shipmentreject(authoritygroup,userId,ShipmentPlanNumber,approvalcoments),HttpStatus.CREATED);
	}
	
	@GetMapping(path = "sendmsg/{shipmentPlanNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendNotification(@PathVariable String shipmentPlanNo) {
		
		logger.info("Called SuccessFully cancelShipmentPlanapprovesendNotification() Controller: ");
		
		return new ResponseEntity<Boolean>(cancelShipmentPlanApprovalService.sendNotifications(shipmentPlanNo), HttpStatus.OK);
	}

}
