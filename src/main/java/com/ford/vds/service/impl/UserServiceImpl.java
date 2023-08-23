package com.ford.vds.service.impl;

import java.io.IOException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.domain.User;
import com.ford.vds.model.UserDTO;
import com.ford.vds.repository.UserRepository;
import com.ford.vds.service.UserService;
import com.ford.vds.webservice.HttpUtil;
import com.ford.vds.webservice.ProcessShipmentResponse;
import com.ford.vds.webservice.ShipmentPlanRequest;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	private HttpUtil http;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserDTO getUserById(String userId) {
		
		logger.info("Inside getUserById() Method: ");
		
		Optional<User> user = userRepository.findById(userId);
		UserDTO userDto = new UserDTO();
		if (user.isPresent()) {
			modelMapper.map(user.get(), userDto);
			logger.info("Login_User:-----> "+userId);
			logger.info("User_Details:-----> "+userDto);
		}
		
		logger.info("Exiting getUserById() Method: ");
		
		return userDto;
	}

	@Override
	public String getAccessLoginScreen() {
		
		logger.info("Inside getAccessLoginScreen() Method: ");
		
		String screenAccess = userRepository.findScreenAccess(VDSConstant.VDS_LOGIN_SCREEN_ACCESS);
		logger.info("Login Screen View:------> "+screenAccess);
		
		logger.info("Exiting getAccessLoginScreen() Method: ");
		
		return screenAccess;
	}

	@Override
	public UserDTO getEmployeeID(String empID) {
		
		logger.info("Inside getEmployeeID() Method: ");
		
		Optional<User> user = userRepository.findByEmployeeId(empID);
		UserDTO userDto = new UserDTO();
		if (user.isPresent()) {
			modelMapper.map(user.get(), userDto);
			logger.info("Login_User:-----> "+empID);
			logger.info("User_Details:-----> "+userDto);
			
		}
		
		logger.info("Inside getEmployeeID() Method: ");
		
		return userDto;
	}

	@Override
	public ProcessShipmentResponse updateAuthorityGroup(String authGroup, String userId) {
		logger.info("Inside updateAuthorityGroup() Method: ");

		ProcessShipmentResponse response = new ProcessShipmentResponse();
		ShipmentPlanRequest request = new ShipmentPlanRequest();
		request.setAuthorityGroup(authGroup);
		request.setUserId(userId);
		request.setProcessName(VDSConstant.UPDATE_AUTHORITY_GROUP);
		try {
			logger.info("Update_AuthorityGroup_Request:-----> " + request);
			response = http.callWebService(request);
			logger.info("Update_AuthorityGroup_Response:-----> " + response);

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Exiting updateAuthorityGroup() Method: ");

		return response;
	}
}
