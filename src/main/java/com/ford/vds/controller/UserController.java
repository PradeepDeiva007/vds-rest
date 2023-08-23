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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ford.vds.model.UserDTO;
import com.ford.vds.service.UserService;
import com.ford.vds.webservice.ProcessShipmentResponse;


@Controller()
@RequestMapping("user/")
public class UserController {

	@Autowired
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
			

	@GetMapping(path = "{userid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> handleSearchShipment(@PathVariable(required = false) String userid) {
		
		logger.info("Called Successfully handleSearchShipment() Controller: ");
		
		return new ResponseEntity<UserDTO>(userService.getUserById(userid), HttpStatus.OK);
	}
	
}
