package com.ford.vds.service;

import com.ford.vds.model.UserDTO;
import com.ford.vds.webservice.ProcessShipmentResponse;

public interface UserService {

	UserDTO getUserById(String userId);

	String getAccessLoginScreen();
	
	UserDTO getEmployeeID(String empID);

	ProcessShipmentResponse updateAuthorityGroup(String authGroup, String userId);
	
	
}
