package com.ford.vds.dao;

import java.util.List;

import com.ford.vds.model.ShipmentPlanGenerationDTO;
import com.ford.vds.model.SummaryChartPlanGenDTO;

public interface ShipmentPlanGenerationDao {

	List<ShipmentPlanGenerationDTO> getDraftPlan(String authGroup);

	List<SummaryChartPlanGenDTO> getSummaryChart1_Details(String authGroup,String screenName,String[] planNumbers);

	List<SummaryChartPlanGenDTO> getSummaryChart2_Details(String authGroup,String screenName,String[] planNumbers);

	String[] getUserAddress(String shipmentPlanNo);

}
