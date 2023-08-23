package com.ford.vds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ford.vds.model.ShipmentPlanDTO;
import com.ford.vds.service.ShipmentService;

@Controller()
@RequestMapping("shipment/")
public class ShipmentController {

	@Autowired
	private ShipmentService shipmentService;

	@GetMapping(path = "search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ShipmentPlanDTO>> handleSearchShipment(
			@RequestParam(required = false, name = "load") List<String> loadId,
			@RequestParam(required = false, name = "city") List<String> city,
			@RequestParam(required = false, name = "transport") List<String> location,
			@RequestParam(required = false, name = "transport") List<String> transportMode,
			@RequestParam(required = false, name = "vlsp") List<String> vlsp) {

		return new ResponseEntity<List<ShipmentPlanDTO>>(
				shipmentService.searchShipmentPlan(loadId, city, location, transportMode, vlsp), HttpStatus.OK);
	}

	@GetMapping(path = "summarychart/{chartNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ShipmentPlanDTO>> handleSummaryChart(@PathVariable String chartNo) {
		return new ResponseEntity<List<ShipmentPlanDTO>>(shipmentService.getSummaryChart(chartNo), HttpStatus.OK);
	}

}
