package com.ford.vds.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class LookupDTO{


	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	public LookupDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public LookupDTO(Long id, Long name) {
		super();
		this.id = id;
		this.name = name.toString();
	}

}
