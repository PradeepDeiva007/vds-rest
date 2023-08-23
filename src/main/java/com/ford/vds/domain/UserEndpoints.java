package com.ford.vds.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "USER_ENDPOINTS", schema = "CFMAVDS")
@Data
@EqualsAndHashCode(callSuper=false)
@SequenceGenerator(name="USER_ENDPOINTS_SEQ", sequenceName="USER_ENDPOINTS_SEQ")
public class UserEndpoints {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="USER_ENDPOINTS_SEQ")
	@Column(name = "ENDPOINT_ROW_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "ENDPOINT")
	private String endpoint;

	@Column(name = "P256DH_KEY")
	private String p256dhkey;

	@Column(name = "AUTH_KEY")
	private String authKey;

}
