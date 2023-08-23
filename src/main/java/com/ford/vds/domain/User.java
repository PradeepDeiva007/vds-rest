package com.ford.vds.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "KVDSM02_USERMASTER", schema = "CFMAVDS")
@Data
public class User {

	@Id
	@Column(name = "VDSM02_USERID_D", updatable = false, nullable = false)
	private String id;

	@Column(name = "VDSM02_USERNAME_N")
	private String username;

	@Column(name = "VDSM02_LOCATION_C")
	private String location;

	@Column(name = "VDSM02_EMAIL_ID")
	private String email;

	@Column(name = "VDSM02_AUTHORITY_GROUP")
	private String authorityGroup;

	@Column(name = "VDSM02_MULTI_AUTHORITY_GROUP")
	private String multiAuthorityGroup;
	
	@Column(name = "VDSM02_EMPLOYEE_ID")
	private String employeeID;

}
