package com.ford.vds.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@JsonSerialize
public class SummaryChartPlanGenDTO {

	private String city;
	private String model;
	private String quantity;
	private String transportMode;
	private List<String> sourceCity;	

}
