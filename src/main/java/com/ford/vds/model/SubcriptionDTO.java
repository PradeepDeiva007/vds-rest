package com.ford.vds.model;

import lombok.Data;

@Data
public class SubcriptionDTO {
	
	private Long id;

	private String userId;

	private String endpoint;
	
	private String p256dhkey;
	
	private String authKey;
}
