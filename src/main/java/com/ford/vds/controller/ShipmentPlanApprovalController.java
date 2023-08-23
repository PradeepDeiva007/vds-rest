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
import com.ford.vds.service.ShipmentPlanApprovalService;
import com.ford.vds.webservice.ProcessShipmentResponse;

@Controller
@RequestMapping("shipmentapproval/")
public class ShipmentPlanApprovalController {
	
	@Autowired
	private ShipmentPlanApprovalService shipmentPlanApprovalService;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentPlanApprovalController.class);
	
	@GetMapping(path = "search/{authoritygroup}", params = {"user"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> handleSearchShipment(@PathVariable String authoritygroup, @RequestParam(value="user") String userId ) {
		
		logger.info("Called SuccessFully loadShipmentPlanapprovalwaiting() Controller: ");
		return new ResponseEntity<List<String>>(
				shipmentPlanApprovalService.loadShipmentPlanapprovalwaiting(authoritygroup,userId), HttpStatus.OK);
	}
	
	@PostMapping(path = "approve/{authoritygroup}", params = {"id","comment"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> approveshipmentplan(@PathVariable String authoritygroup,
			@RequestParam(value="id") String userId,@RequestParam(value="comment") String approvalcoments,@RequestBody String[] ShipmentPlanNumber) {
		
		logger.info("Called SuccessFully shipmentapprove() Controller: ");
		return new ResponseEntity<ProcessShipmentResponse>(shipmentPlanApprovalService.shipmentapprove(authoritygroup,userId,ShipmentPlanNumber,approvalcoments),HttpStatus.CREATED);
	}
	
	
	@PostMapping(path = "reject/{authoritygroup}",params= {"id","comment"} ,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> rejectShipmentplan(@PathVariable String authoritygroup,
			@RequestParam(value="id") String userId,@RequestParam(value="comment") String approvalcoments, @RequestBody String[] ShipmentPlanNumber) {
		
		logger.info("Called SuccessFully shipmentreject() Controller: ");
		return new ResponseEntity<ProcessShipmentResponse>(shipmentPlanApprovalService.shipmentreject(authoritygroup,userId,ShipmentPlanNumber,approvalcoments),HttpStatus.CREATED);
	}

}
