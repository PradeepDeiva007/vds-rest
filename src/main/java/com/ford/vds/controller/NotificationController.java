package com.ford.vds.controller;

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

import com.ford.vds.model.SubcriptionDTO;
import com.ford.vds.service.NotificationService;

@Controller()
@RequestMapping("notification/")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	private Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@PostMapping(path = "subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> handleSubscribe(@RequestBody SubcriptionDTO dto) {
		
		logger.info("Called Successfully handleSubscribe() Controller: ");

		return new ResponseEntity<Boolean>(notificationService.subscribeUser(dto), HttpStatus.OK);
	}

	@PostMapping(path = "unsubscribe", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> handleUnsubscribe(@RequestBody SubcriptionDTO dto) {
		
		logger.info("Called Successfully handleUnsubscribe() Controller: ");
		
		return new ResponseEntity<Boolean>(notificationService.unsubscribeUser(dto), HttpStatus.OK);
	}

	@GetMapping(path = "sendmsg", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendNotification() {
		
		logger.info("Called Successfully sendNotification() Controller: ");
		
		return new ResponseEntity<Boolean>(notificationService.sendNotifications(), HttpStatus.OK);
	}
	
	@PostMapping(path = "sendNotification/{process}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> sendNotificationToUser(@PathVariable String process,@RequestBody String[] shipmentPlanNo) {
		
		logger.info("Called Successfully sendNotificationToUser() Controller: ");
		
		return new ResponseEntity<Boolean>(notificationService.sendShipmentPlanNotification(process,shipmentPlanNo), HttpStatus.OK);
	}
}
