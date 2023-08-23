package com.ford.vds.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.ShipmentPlanGenerationDao;
import com.ford.vds.model.DraftPlanDetailsDTO;
import com.ford.vds.model.ShipmentPlanGenerationDTO;
import com.ford.vds.model.SummaryChartPlanGenDTO;

@Repository
public class ShipmentPlanGenerationDaoImpl implements ShipmentPlanGenerationDao {

	@PersistenceContext
	private EntityManager em;
	
	private static Logger logger = LoggerFactory.getLogger(ShipmentPlanGenerationDaoImpl.class);
	
	private List<DraftPlanDetailsDTO> exportDraftPlanList = new ArrayList<DraftPlanDetailsDTO>();

	@Override
	@SuppressWarnings("unchecked")
	public List<ShipmentPlanGenerationDTO> getDraftPlan(String authGroup) {
		
		logger.info("Inside getDraftPlan() Method:");

		String vinTable = "KVDST01_VIN_";
		if (authGroup != null) {
			vinTable += authGroup.substring(0, 2);
		}

		List<ShipmentPlanGenerationDTO> shipmentPlan = new ArrayList<ShipmentPlanGenerationDTO>();
		StringBuffer spQuery = new StringBuffer();

		spQuery.append("SELECT DISTINCT SPLAN.VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENTPLANNUMBER_C, VDST02_LOADNUMBER_R,VDST01_PLANNED_LOCATION, ");
		spQuery.append("VDST02_PLANNED_VLSP_C  VLSP_NAME, SPLAN.VDST02_PLANNED_TRANSPORTMODE_C AS TRANSPORT_MODE, ");
		
		spQuery.append(" CASE WHEN VIN.VDST01_PLANNED_LOCATION = L.VDST06_LOCATION_C ");
		spQuery.append(" AND VIN.VDST01_LOADNUMBER_R = VDST02_LOADNUMBER_R THEN COUNT(VIN.VDST01_VIN_C) ELSE COUNT(VIN.VDST01_VIN_C) ");
		spQuery.append(" END AS PLANNED_CAPACITY, ");
		
		//spQuery.append("SUM(SPLAN.VDST02_LOADQUANTITY_Q) AS PLANNED_CAPACITY, ");
		
		//spQuery.append("CASE WHEN UPPER(TMODE.VDSM05_STAND_TRANS_MODE) = 'COMBINED MODE' THEN ");
		//spQuery.append("SUBSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER, 1, INSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER, '~') - 1) ELSE ");
		//spQuery.append("SPLAN.VDST02_PLANNED_TRANSPORTMODE_C END AS TRANSPORT_MODE, ");
		
		//spQuery.append("(SELECT VDSM17_CITY_CHINESE_NAME FROM KVDSM17_CITY WHERE VDSM17_CITY_NAME = L.SOURCE_CITY)  SOURCE_CITY_CHINESE,L.SOURCE_CITY SOURCE_CITY ");
		
		 spQuery.append(" (SELECT NVL(SUBSTR(VDSM17_CITY_CHINESE_NAME,1,INSTR(VDSM17_CITY_CHINESE_NAME,'-')-1),VDSM17_CITY_CHINESE_NAME) FROM KVDSM17_CITY WHERE VDSM17_CITY_NAME = L.DESTINATION_CITY)  DESTINATION_CITY_CHINESE,NVL(SUBSTR(L.DESTINATION_CITY,1,INSTR(L.DESTINATION_CITY,'-')-1),L.DESTINATION_CITY) DESTINATION_CITY ");  
		
		/* spQuery.append(" CASE WHEN SPLAN.VDST02_SHIPMENTPLANTYPE_C = 'K' ");
		spQuery.append(" THEN ");
		spQuery.append(" (SELECT NVL(SUBSTR(VDSM17_CITY_CHINESE_NAME,1,INSTR(VDSM17_CITY_CHINESE_NAME,'-')-1),VDSM17_CITY_CHINESE_NAME) ");
		spQuery.append(" FROM KVDSM17_CITY WHERE VDSM17_CITY_NAME = L.DESTINATION_CITY) ");
		spQuery.append(" ELSE ");
		spQuery.append(" (SELECT VDSM17_CITY_CHINESE_NAME FROM KVDSM17_CITY WHERE VDSM17_CITY_NAME = L.DESTINATION_CITY) ");
		spQuery.append(" END AS DESTINATION_CITY_CHINESE, ");
		spQuery.append(" NVL(SUBSTR(L.DESTINATION_CITY,1,INSTR(L.DESTINATION_CITY,'-')-1),L.DESTINATION_CITY) DESTINATION_CITY "); */

		spQuery.append("FROM KVDST02_SHIPMENTPLAN  SPLAN JOIN KVDSM05_TRANSPORTMODE  TMODE ON TMODE.VDSM05_TRANSPORTMODE_X = SPLAN.VDST02_PLANNED_TRANSPORTMODE_C ");
		spQuery.append("JOIN KVDSM35_TRANSPORT_AUTH  TAUTH ON TAUTH.VDSM05_TRANSPORTMODE_X = TMODE.VDSM05_TRANSPORTMODE_X AND UPPER(TAUTH.VDSM35_AUTHORITY_GROUP) = '"+ authGroup + "' ");
		spQuery.append("JOIN (SELECT DISTINCT VDST01_SHIPMENTPLANNUMBER_C, VDST01_VIN_C, VDST01_LOADNUMBER_R, VDST01_PLANNED_LOCATION FROM "+ vinTable + " )  VIN ");
		spQuery.append("ON VIN.VDST01_SHIPMENTPLANNUMBER_C = SPLAN.VDST02_SHIPMENTPLANNUMBER_C AND VIN.VDST01_LOADNUMBER_R = SPLAN.VDST02_LOADNUMBER_R ");
		
		spQuery.append("JOIN (SELECT DISTINCT VDST06_SHIPMENTPLANNUMBER_C SHIP_NUMBER, VDST06_LOADNUMBER_R LOAD_NUMBER, VDST06_SOURCECITY_C SOURCE_CITY, VDST06_LOCATION_C, ");
		spQuery.append(" (SELECT VDSM04_CITY_X FROM KVDSM04_DEALER WHERE VDSM04_DEALERCODE_C = VDST06_DEALER_C) DESTINATION_CITY FROM KVDST06_LOADDETAILS LOAD)  L ");
		spQuery.append("ON L.SHIP_NUMBER = SPLAN.VDST02_SHIPMENTPLANNUMBER_C AND L.LOAD_NUMBER = SPLAN.VDST02_LOADNUMBER_R LEFT OUTER JOIN ( ");
		
		spQuery.append("SELECT KVDSM15_VLSP.VDSM15_VLSPNAME   AS VLSP_NAME, VDSM05_TRANSPORTMODE_X VLSP_TMODE, NVL(VDSM16_DAILY_CAPACITY, 0) - NVL(VDSM16_HARDCONSUMED_CAPACITY, 0) VLSP_CAPACITY, ");
		spQuery.append("VLSP_TMODE.VDSM15_VLSP_ID AS VLSP_ID, VDSM16_SOURCE_CITY SOURCE_CITY FROM KVDSM16_VLSP_TRANSPORT VLSP_TMODE JOIN KVDSM15_VLSP ON KVDSM15_VLSP.VDSM15_VLSP_ID = VLSP_TMODE.VDSM15_VLSP_ID ");
		spQuery.append("WHERE VDSM16_ACTIVE = 'Y')  E ON E.VLSP_NAME = SPLAN.VDST02_PLANNED_VLSP_C AND E.VLSP_TMODE = (CASE WHEN UPPER(TMODE.VDSM05_STAND_TRANS_MODE) = 'COMBINED MODE' THEN ");
		spQuery.append("SUBSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER, 1, INSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER, '~') - 1) ELSE SPLAN.VDST02_PLANNED_TRANSPORTMODE_C END ) AND E.SOURCE_CITY = L.SOURCE_CITY ");
		spQuery.append("LEFT OUTER JOIN (SELECT VDST02_PLANNED_VLSP_C  VLSP_NAME, VDST02_PLANNED_TRANSPORTMODE_C  TMODE, SUM(SHIP.VDST02_EXCEEDEDLOADQTY_Q)  EXCEEDED, L.SOURCE_CITY ");
		spQuery.append("FROM KVDST02_SHIPMENTPLAN  SHIP JOIN (SELECT DISTINCT VDST06_SHIPMENTPLANNUMBER_C   SHIP_NUMBER,VDST06_LOADNUMBER_R  LOAD_NUMBER,VDST06_SOURCECITY_C  SOURCE_CITY ");
		spQuery.append("FROM KVDST06_LOADDETAILS LOAD)  L ON L.SHIP_NUMBER = SHIP.VDST02_SHIPMENTPLANNUMBER_C AND L.LOAD_NUMBER = SHIP.VDST02_LOADNUMBER_R ");
		spQuery.append("WHERE SHIP.VDST02_STATUS_C = 'D' AND SHIP.VDST02_APPROVE_F IS NULL AND SHIP.VDST02_ISEXCEEDEDCAPACITY = 'Y' GROUP BY VDST02_PLANNED_VLSP_C, VDST02_PLANNED_TRANSPORTMODE_C, L.SOURCE_CITY)  S ");
		spQuery.append("ON S.VLSP_NAME = SPLAN.VDST02_PLANNED_VLSP_C AND S.TMODE = SPLAN.VDST02_PLANNED_TRANSPORTMODE_C AND S.SOURCE_CITY = L.SOURCE_CITY JOIN KVDSM15_VLSP  VLSP ON VDST02_PLANNED_VLSP_C = VLSP.VDSM15_VLSPNAME ");
		spQuery.append("WHERE SPLAN.VDST02_STATUS_C = 'D' AND NVL(SPLAN.VDST02_APPROVE_F, 'N') = 'N' ");
		
		spQuery.append("GROUP BY SPLAN.VDST02_SHIPMENTPLANNUMBER_C, E.VLSP_ID, VDST02_PLANNED_VLSP_C, TMODE.VDSM05_STAND_TRANS_MODE, ");
		spQuery.append("VDST02_PLANNED_TRANSPORTMODE_C,TAUTH.VDSM05_COMBINEDMODE_ORDER, L.SOURCE_CITY, VDST01_PLANNED_LOCATION, VDST02_LOADNUMBER_R, L.DESTINATION_CITY ");
		spQuery.append(",L.VDST06_LOCATION_C, VIN.VDST01_LOADNUMBER_R ");
		//spQuery.append(" ,SPLAN.VDST02_SHIPMENTPLANTYPE_C ");
		
		spQuery.append("ORDER BY VDST02_PLANNED_VLSP_C, VDST02_PLANNED_TRANSPORTMODE_C ");
		
		logger.info("Draft_Plan Query: "+spQuery.toString());
		
		StringBuffer sqlQuery = new StringBuffer();
		
		sqlQuery.append("SELECT DISTINCT E.SHIPMENT_NUMBER, E.CREATED_BY, SUM(DELIVERY_COST) DELIVERY_COST, COUNT(E.VDST01_VIN_C)");
		sqlQuery.append("FROM  (SELECT VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENT_NUMBER, VDST02_CREATEUSER_C AS CREATED_BY, ");
		sqlQuery.append("VDST01_VIN_C, (NVL(VDST01_VIN_COST,0) + NVL(VDST01_INSURANCE_COST,0)) DELIVERY_COST  FROM KVDST02_SHIPMENTPLAN SP ");
		sqlQuery.append("JOIN "+ vinTable + " VIN ON VIN.VDST01_SHIPMENTPLANNUMBER_C = SP.VDST02_SHIPMENTPLANNUMBER_C  AND VIN.VDST01_LOADNUMBER_R = SP.VDST02_LOADNUMBER_R ");
		sqlQuery.append("WHERE VDST02_STATUS_C = 'D'  AND NVL(VDST02_APPROVE_F,'N')='N' ) E  GROUP BY  E.SHIPMENT_NUMBER,CREATED_BY ");
		
		logger.info("Draft_Plan Details Query: "+sqlQuery.toString());
		
		try {
			Query query1 = em.createNativeQuery(spQuery.toString());
			List<Object[]> result = query1.getResultList();

			Query query2 = em.createNativeQuery(sqlQuery.toString());
			List<Object[]> detailsResult = query2.getResultList();
			String[] values = new String[4];
			for (Object[] obj : detailsResult) {
				values[0] = obj[0].toString();
				values[1] = obj[1].toString();
				values[2] = obj[2].toString();
				values[3] = obj[3].toString();
			}

			getDetailsToExportExcel(values.length > 0 ? values[0] : "");

			for (Object[] object : result) {
				ShipmentPlanGenerationDTO dto = new ShipmentPlanGenerationDTO();

				dto.setLoadNo(object[1] != null
						? (String.format(object[0].toString() + "%04d", Integer.parseInt(object[1].toString())))
						: null);
				dto.setCity(object[6] != null ? object[6].toString() : null);
				dto.setPlannedLocation(object[2] != null ? object[2].toString() : null);
				dto.setShipmentPlanNo(object[0] != null ? object[0].toString() : null);
				dto.setTransportMode(object[4] != null ? object[4].toString() : null);
				dto.setVlsp(object[3] != null ? object[3].toString() : null);
				dto.setQuantity(object[5] != null ? object[5].toString() : null);
				dto.setGeneratedBy(values[1]);
				dto.setTotalDeliveryCost(values[2]);
				dto.setTotalVins(values[3]);
				dto.setExportDraftPlan(exportDraftPlanList);
				shipmentPlan.add(dto);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		logger.info("Exiting getDraftPlan() Method:");
		
		return shipmentPlan;
	}

	private void getDetailsToExportExcel(String shipmentPlanNo) {
		
		logger.info("Inside getDetailsToExportExcel() Method:");
		
		String vintablename =null;
		if (shipmentPlanNo != null) {
			if (shipmentPlanNo.startsWith("CQ")) {
				vintablename = "KVDST01_VIN_CQ";
			} else if (shipmentPlanNo.startsWith("HZ")) {
				vintablename = "KVDST01_VIN_HZ";
			} else if (shipmentPlanNo.startsWith("HB")) {
				vintablename = "KVDST01_VIN_HB";
			}
		}
		
		StringBuffer exportQuery = new StringBuffer();
		exportDraftPlanList.clear();
		
		if(shipmentPlanNo != null && !shipmentPlanNo.equals("")) {
			exportQuery.append(" SELECT VDST02_SHIPMENTPLANNUMBER_C,VIN.VDST01_VIN_C AS VIN_NUMBER,CAT.VDSM06_MODEL_X AS MODEL_CODE ,VIN.VDST01_CATEGORY_C AS CATEGORY_CODE, ");
			exportQuery.append(" VIN.VDST01_ALLOCATIONDATE_S AS ALLOCATIONDATE_S, VIN.VDST01_PLANNED_LOCATION AS PLANNED_LOCATION, ");
			exportQuery.append(" VDST02_VLSP_C, NVL(VIN.VDST01_VIN_COST,0) + NVL(VIN.VDST01_INSURANCE_COST,0) AS VIN_COST, ");
			exportQuery.append(" (NVL(VIN.VDST01_VIN_COST,0) - NVL(VIN.VDST01_ACTUAL_VIN_COST_MILK,0)) AS COMPENSATION_COST, VIN.VDST01_SHIPTODEALER_C AS DEALER, ");
			exportQuery.append(" (SELECT VDSM17_CITY_CHINESE_NAME  FROM KVDSM17_CITY WHERE VDSM17_CITY_NAME=DEALER.VDSM04_CITY_X)  AS CITY, ");
			exportQuery.append(" VIN.VDST01_LOADNUMBER_R AS LOADNUMBER_R, VIN.VDST01_SHIPTODEALER_C AS SHIPTODEALER_C, DEALER.VDSM04_DEALERNAME_N AS DEALER_NAME, ");
			exportQuery.append(" VDST02_PLANNED_TRANSPORTMODE_C ACTUAL_DELIVERY_MODE,VDST02_SHIPMENTPLANTYPE_C , NVL(VDST01_EXPORT_VIN_F,'N') AS VDST01_EXPORT_VIN_F, ");
			exportQuery.append(" VIN.VDST01_SHIPSCHEDULE_DT AS SHIPSCHEDULE_DT  FROM "+vintablename+"  VIN, KVDSM06_CATEGORYCODE CAT, KVDSM04_DEALER DEALER, ");
			exportQuery.append(" KVDST02_SHIPMENTPLAN WHERE VIN.VDST01_CATEGORY_C = CAT.VDSM06_CATEGORYCODE_C AND VIN.VDST01_SHIPTODEALER_C = DEALER.VDSM04_DEALERCODE_C ");
			exportQuery.append(" AND VDST01_SHIPMENTPLANNUMBER_C = VDST02_SHIPMENTPLANNUMBER_C AND VDST01_LOADNUMBER_R = VDST02_LOADNUMBER_R AND ");
			exportQuery.append(" VDST01_SHIPMENTPLANNUMBER_C = '"+ shipmentPlanNo +"' ORDER BY VDST01_LOADNUMBER_R,DEALER.VDSM04_CITY_X,CAT.VDSM06_MODEL_X,VIN.VDST01_VIN_C ");
			
			logger.info("Export_Excel Query: "+exportQuery.toString());
			
			try {
				Query query3 = em.createNativeQuery(exportQuery.toString());
				List<Object[]> exportResult = query3.getResultList();
				for (Object[] object : exportResult) {
					DraftPlanDetailsDTO draft = new DraftPlanDetailsDTO();

					draft.setShipmentPlanNo(object[0] != null ? object[0].toString() : "");
					draft.setLoadNo(object[11] != null ? object[11].toString() : "");
					draft.setVinNo(object[1] != null ? object[1].toString() : "");
					draft.setVinCost(object[7] != null ? object[7].toString() : "");
					draft.setPlannedLocation(object[5] != null ? object[5].toString() : "");
					draft.setDealer(object[9] != null ? object[9].toString() : "");
					draft.setDestinationCity(object[10] != null ? object[10].toString() : "");
					draft.setModel(object[2] != null ? object[2].toString() : "");
					draft.setVlsp(object[6] != null ? object[6].toString() : "");
					draft.setTransportMode(object[14] != null ? object[14].toString() : "");

					exportDraftPlanList.add(draft);
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		logger.info("Exiting getDetailsToExportExcel() Method:");
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SummaryChartPlanGenDTO> getSummaryChart1_Details(String authGroup, String screen, String[] planList) {
		
		logger.info("Inside getSummaryChart1_Details() Method:");
		
		String vinTable = "KVDST01_VIN_";
		String planNumbers = "";
		if (authGroup != null && screen.equals("Generation")) {
			vinTable += authGroup.substring(0, 2);
		} else if (authGroup != null && screen.equals("Approval") || screen.equals("CancelApproval")) {
			vinTable = "KVDST01_VIN";
			planNumbers = convertStringFormat(planList);
		}
		
		StringBuffer chartQuery = new StringBuffer();
		
		chartQuery.append("SELECT CHART.VDSM06_MODEL_X, VIN.VDST01_PLANNED_LOCATION, COUNT(VIN.VDST01_VIN_C) FROM "+ vinTable + "  VIN ");
		chartQuery.append("JOIN (SELECT VDST01_VIN_C, VDSM06_MODEL_X, VDST06_SOURCECITY_C FROM "+ vinTable + " ");
		chartQuery.append("JOIN KVDSM06_CATEGORYCODE ON VDSM06_CATEGORYCODE_C = VDST01_CATEGORY_C ");
		chartQuery.append("JOIN KVDST06_LOADDETAILS ON VDST01_SHIPMENTPLANNUMBER_C = VDST06_SHIPMENTPLANNUMBER_C AND VDST06_LOADNUMBER_R = VDST01_LOADNUMBER_R ");
		chartQuery.append("GROUP BY VDST01_VIN_C, VDSM06_MODEL_X, VDST06_SOURCECITY_C)  CHART ON CHART.VDST01_VIN_C = VIN.VDST01_VIN_C ");
		
		if (planNumbers != null && planNumbers.length() > 0) {
			chartQuery.append("AND VIN.VDST01_SHIPMENTPLANNUMBER_C IN (" + planNumbers + ") ");
		}
		
		chartQuery.append("GROUP BY CHART.VDSM06_MODEL_X, VIN.VDST01_PLANNED_LOCATION ");
		
		logger.info("Summary_Chart_1 Query: "+chartQuery.toString());
		List<SummaryChartPlanGenDTO> summaryList = new ArrayList<>();
		try {
			Query query = em.createNativeQuery(chartQuery.toString());
			List<Object[]> result = query.getResultList();
			for (Object[] object : result) {
				SummaryChartPlanGenDTO dto = new SummaryChartPlanGenDTO();
				Set<String> sourceCities = new HashSet<>();
				for (Object[] obj : result) {
					if ((object[0].toString()).equalsIgnoreCase(obj[0].toString())) {
						sourceCities.add(obj[1].toString() + "~" + obj[2].toString());
					} else {
						sourceCities.add(obj[1].toString() + "~0");
					}
				}

				List<String> list = getCrctFormatOfList(sourceCities);
				dto.setModel(object[0] != null ? object[0].toString() : null);
				dto.setCity(object[1] != null ? object[1].toString() : null);
				dto.setQuantity(object[2] != null ? object[2].toString() : null);
				dto.setSourceCity(list);
				summaryList.add(dto);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		logger.info("Exiting getSummaryChart1_Details() Method:");
		
		return summaryList;
	}

	private String convertStringFormat(String[] planList) {
		
		logger.info("Inside convertStringFormat() Method:");
		
		int i = 0;
		StringBuffer str = new StringBuffer();
		for (String s : planList) {
			if ((planList.length) - 1 != i) {
				str.append("'" + s + "',");
				i++;
			} else {
				str.append("'" + s + "'");
			}
		}
		
		logger.info("Exiting convertStringFormat() Method:");
		
		return str.toString();
	}

	private int getTotalCount(List<String> list) {

		logger.info("Inside getTotalCount() Method:");
		
		int count = 0;
		for (String l : list) {
			String[] value = l.split("~");
			count += Integer.parseInt(value[1]);
		}
		
		logger.info("Exiting getTotalCount() Method:");
		
		return count;
	}

	private List<String> getCrctFormatOfList(Set<String> sourceCities) {
		
		logger.info("Inside getCrctFormatOfList() Method:");

		List<String> list = new ArrayList<>();
		String[] myArray = new String[sourceCities.size()];
		sourceCities.toArray(myArray);
		String[] st = new String[myArray.length];
		int flag = 0;
		for (int i = 0; i < myArray.length; i++) {
			String a[] = myArray[i].split("~");
			for (int j = i + 1; j < myArray.length; j++) {
				String b[] = myArray[j].split("~");
				if (a[0].equals(b[0])) {
					list.add(a[1].equals("0") ? myArray[j] : myArray[i]);
					st[i] = a[1].equals("0") ? b[0] : a[0];
					flag = 1;
				}
			}
			if (!list.contains(myArray[i]) && flag == 0 && !Arrays.asList(st).contains(a[0])) {
				list.add(myArray[i]);
			}
			flag = 0;
		}
		Collections.sort(list);
		
		logger.info("Exiting getCrctFormatOfList() Method:");
		
		return list;
	}

	@Override
	public List<SummaryChartPlanGenDTO> getSummaryChart2_Details(String authGroup, String screen, String[] planList) {

		logger.info("Inside getSummaryChart2_Details() Method:");
		
		String vinTable = "KVDST01_VIN_";
		String planNumbers = "";
		if (authGroup != null && screen.equals("Generation")) {
			vinTable += authGroup.substring(0, 2);
		} else if (authGroup != null && screen.equals("Approval") || screen.equals("CancelApproval")) {
			vinTable = "KVDST01_VIN";
			planNumbers = convertStringFormat(planList);
		}

		StringBuffer chartQuery = new StringBuffer();
		
//		chartQuery.append("SELECT MAX(VIN.VDST01_LOADNUMBER_R), CHART.VDST02_TRANSPORTMODE_C, CHART.VDST06_SOURCECITY_C, COUNT(VIN.VDST01_VIN_C) ");
//		chartQuery.append("FROM "+ vinTable + " VIN JOIN ( SELECT VDST01_VIN_C, VDST02_TRANSPORTMODE_C, VDST06_SOURCECITY_C FROM "+ vinTable + " ");
//		chartQuery.append("JOIN KVDST02_SHIPMENTPLAN ON VDST01_SHIPMENTPLANNUMBER_C = VDST02_SHIPMENTPLANNUMBER_C ");
//		chartQuery.append("JOIN KVDST06_LOADDETAILS ON VDST01_SHIPMENTPLANNUMBER_C = VDST06_SHIPMENTPLANNUMBER_C AND VDST06_LOADNUMBER_R = VDST01_LOADNUMBER_R ");
//		chartQuery.append("WHERE VDST02_STATUS_C = 'D' ");
//		if (planNumbers != null && planNumbers.length() > 0) {
//			chartQuery.append(" AND VDST02_APPROVE_F IN('S','M') ");
//			chartQuery.append(" GROUP BY VDST01_VIN_C, VDST02_TRANSPORTMODE_C, VDST06_SOURCECITY_C) CHART ON CHART.VDST01_VIN_C = VIN.VDST01_VIN_C ");
//			chartQuery.append("AND VIN.VDST01_SHIPMENTPLANNUMBER_C IN (" + planNumbers + ") ");
//		}else {
//			chartQuery.append(" AND VDST02_APPROVE_F is null ");
//			chartQuery.append(" GROUP BY VDST01_VIN_C, VDST02_TRANSPORTMODE_C, VDST06_SOURCECITY_C) CHART ON CHART.VDST01_VIN_C = VIN.VDST01_VIN_C ");
//		}
//		chartQuery.append("GROUP BY CHART.VDST02_TRANSPORTMODE_C, CHART.VDST06_SOURCECITY_C ");
		
		chartQuery.append(" SELECT MAX(LOAD_NUMBER), TRANSPORT_MODE,PLANNED_LOCATION,COUNT(VIN) FROM ");
		chartQuery.append(" (SELECT LD.VDST06_LOADNUMBER_R LOAD_NUMBER, CQ.VDST01_VIN_C VIN, ");
		chartQuery.append(" SP.VDST02_PLANNED_TRANSPORTMODE_C TRANSPORT_MODE, VDST01_PLANNED_LOCATION PLANNED_LOCATION ");
		chartQuery.append(" FROM "+ vinTable + " CQ JOIN KVDST06_LOADDETAILS LD ");
		chartQuery.append(" ON CQ.VDST01_SHIPMENTPLANNUMBER_C = VDST06_SHIPMENTPLANNUMBER_C ");
		chartQuery.append(" AND  LD.VDST06_LOADNUMBER_R = CQ.VDST01_LOADNUMBER_R ");
		chartQuery.append(" JOIN KVDST02_SHIPMENTPLAN SP ON SP.VDST02_LOADNUMBER_R = LD.VDST06_LOADNUMBER_R ");
		chartQuery.append("  AND SP.VDST02_SHIPMENTPLANNUMBER_C = LD.VDST06_SHIPMENTPLANNUMBER_C ");
		
		if(planNumbers != null && planNumbers.length() > 0) {
		chartQuery.append(" WHERE VDST01_PLAN_F = 'Y' AND VDST02_STATUS_C = 'D' AND VDST02_APPROVE_F IN ('S','M') ");
		chartQuery.append(" AND CQ.VDST01_SHIPMENTPLANNUMBER_C IN (" + planNumbers + ") ");
		}else {
			chartQuery.append(" WHERE VDST01_PLAN_F = 'D' AND VDST02_STATUS_C = 'D' ");
			chartQuery.append(" AND VDST02_APPROVE_F IS NULL ");
		}
		
		chartQuery.append(" GROUP BY LD.VDST06_LOADNUMBER_R, CQ.VDST01_VIN_C, SP.VDST02_PLANNED_TRANSPORTMODE_C, ");
		chartQuery.append(" VDST01_PLANNED_LOCATION) CHART GROUP BY TRANSPORT_MODE, PLANNED_LOCATION ");
		
		List<SummaryChartPlanGenDTO> summaryList = new ArrayList<>();
		
		logger.info("Summary_Chart_2 Query: "+chartQuery.toString());

		try {
			Query query = em.createNativeQuery(chartQuery.toString());
			List<Object[]> result = query.getResultList();

			for (Object[] object : result) {

				SummaryChartPlanGenDTO dto = new SummaryChartPlanGenDTO();
				Set<String> sourceCities = new HashSet<>();
				for (Object[] obj : result) {
					if ((object[1].toString()).equalsIgnoreCase(obj[1].toString())) {
						sourceCities.add(obj[2].toString() + "~" + obj[3].toString());
					} else {
						sourceCities.add(obj[2].toString() + "~0");
					}
				}

				List<String> list = getCrctFormatOfList(sourceCities);
				dto.setTransportMode(object[1] != null ? object[1].toString() : null);
				dto.setCity(object[2] != null ? object[2].toString() : null);
				dto.setQuantity(object[3] != null ? object[3].toString() : null);
				dto.setSourceCity(list);
				summaryList.add(dto);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		logger.info("Exiting getSummaryChart2_Details() Method:");

		return summaryList;
	}

	@Override
	public String[] getUserAddress(String shipmentPlanNo) {
		
		logger.info("Inside getUserAddress() Method:");
		
		String screenId = VDSConstant.SHIPMENTPLAN_SCREENID;
		StringBuffer sqlQuery = new StringBuffer();
		
		sqlQuery.append("SELECT DISTINCT CRT.VDSM02_USERID_D AS CREATER_EMAIL_ID , ");
		sqlQuery.append("SUP.VDSM02_USERID_D AS  SUPERVISOREMAILID ,APP.VDSM34_SUPERVISOR_USER_F AS SUPERVISORFLAG , ");
		sqlQuery.append("MGR.VDSM02_USERID_D AS MANAGEREMAIL ,APP.VDSM34_MANAGER_USER_F AS MANAGERFLAG , ");
		sqlQuery.append("PLN.VDST02_APPROVE_F AS SHIPMENTAPPROVEFLAG FROM KVDST02_SHIPMENTPLAN PLN ");
		sqlQuery.append("JOIN KVDSM02_USERMASTER CRT ON CRT.VDSM02_USERID_D=PLN.VDST02_CREATEUSER_C ");
		sqlQuery.append("JOIN KVDSM34_USER_SCREEN_APPROVER APP ON APP.VDSM34_AUTHORIY_GROUP=CRT.VDSM02_AUTHORITY_GROUP ");
		sqlQuery.append("JOIN KVDSM02_USERMASTER SUP ON SUP.VDSM02_USERID_D=APP.VDSM34_SUPERVISOR_USER_D ");
		sqlQuery.append("JOIN KVDSM02_USERMASTER MGR ON MGR.VDSM02_USERID_D=APP.VDSM34_MANAGER_USER_D ");
		sqlQuery.append("WHERE PLN.VDST02_SHIPMENTPLANNUMBER_C ='"+ shipmentPlanNo + "' AND APP.VDSM34_SCREENID_D='" + screenId + "' ");
		
		logger.info("GET USER EMAIL_ID Query: "+sqlQuery.toString());
		Query query = em.createNativeQuery(sqlQuery.toString());
		List<Object[]> result = query.getResultList();
		Set<String> list = new HashSet<String>();
		for(Object[] obj : result) {
			list.add(obj[0].toString() != null ? obj[0].toString() : "0");
			list.add(obj[1].toString() != null ? obj[1].toString() : "0");
			list.add(obj[3].toString() != null ? obj[3].toString() : "0");
//			address += obj[0].toString() + "~" + obj[1].toString() + "~" + obj[3].toString();
		}
		list.removeIf(s -> s.equals("0"));
		String[] address = new String[list.size()];
		address = list.toArray(address);
		
		logger.info("Exiting getUserAddress() Method:");
		
		return address;
	}

}
