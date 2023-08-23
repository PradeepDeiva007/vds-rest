package com.ford.vds.model;

import lombok.Data;

@Data
public class UserDTO {

	private String id;

	private String username;

	private String location;

	private String email;

	private String authorityGroup;

	private String multiAuthorityGroup;
	
	private String employeeID;
	
	private String access_token;
	
	private String UserId;
	
	private String DeviceId;

}
