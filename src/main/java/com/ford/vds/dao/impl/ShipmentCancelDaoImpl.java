package com.ford.vds.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ford.vds.dao.ShipmentCancelDao;
import com.ford.vds.model.ShipmentCancelPlanDTO;

@Repository
@Transactional
public class ShipmentCancelDaoImpl implements ShipmentCancelDao {
	@PersistenceContext
	private EntityManager em;
	
	private Logger logger = LoggerFactory.getLogger(ShipmentCancelDaoImpl.class);

	@SuppressWarnings("unchecked")	
	public List<ShipmentCancelPlanDTO> getCancelPlanByParams(String planNo, String authGroupValue)
			throws Exception {
	
	logger.info("Inside getCancelPlanByParams() Method: ");
		
	StringBuilder shipmentcancelQuery = new StringBuilder();

	shipmentcancelQuery.append("SELECT SHIPMENTPLANNUMBER_C,VDST02_LOADNUMBER_R,PLANNED_LOCATION,VLSP_NAME,TRANSPORT_MODE,LOADQUANTITY,CITY FROM ");
	shipmentcancelQuery.append("(SELECT DISTINCT VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENTPLANNUMBER_C,VDST02_LOADNUMBER_R,VIN.VDST01_PLANNED_LOCATION PLANNED_LOCATION, ");
	shipmentcancelQuery.append("VDST02_PLANNED_VLSP_C VLSP_NAME,VDST02_PLANNED_TRANSPORTMODE_C TRANSPORT_MODE,VDST02_LOADQUANTITY_Q   AS LOADQUANTITY, ");
	shipmentcancelQuery.append("(SELECT VDSM17_CITY_CHINESE_NAME  FROM KVDSM17_CITY  WHERE VDSM17_CITY_NAME=VDSM04_CITY_X  ) AS CITY ");
	shipmentcancelQuery.append("FROM KVDST01_VIN VIN LEFT OUTER JOIN KVDST02_SHIPMENTPLAN ON VDST02_SHIPMENTPLANNUMBER_C =VIN.VDST01_SHIPMENTPLANNUMBER_C ");
	shipmentcancelQuery.append("AND VIN.VDST01_LOADNUMBER_R   =VDST02_LOADNUMBER_R LEFT OUTER JOIN KVDSM04_DEALER ON VDSM04_DEALERCODE_C =VIN.VDST01_SHIPTODEALER_C  ");
	shipmentcancelQuery.append("WHERE VDST02_SHIPMENTPLANNUMBER_C ='" + planNo +"' AND VIN.VDST01_STATUS_C ='01' AND VIN.VDST01_VDRNUMBER_C IS NULL AND NVL(VDST02_APPROVE_F,'N') = 'Y' " );
	shipmentcancelQuery.append(" AND SUBSTR(VDST02_SHIPMENTPLANNUMBER_C, 0 ,2) = SUBSTR('" + authGroupValue +"', 0 ,2)) ORDER BY VDST02_LOADNUMBER_R ");
	
	logger.info("Cancel_SP Query: "+shipmentcancelQuery.toString());
	
	Query query = em.createNativeQuery(shipmentcancelQuery.toString());

	List<Object[]> result = query.getResultList();
	List<ShipmentCancelPlanDTO> CancelPlanlist = result.stream().filter(Objects::nonNull).map(object -> {
		ShipmentCancelPlanDTO dto = new ShipmentCancelPlanDTO();
				
		dto.setLoadNo(object[1] != null ? (String.format(object[0].toString() + "%04d", Integer.parseInt(object[1].toString()))) : null);
		dto.setShipmentNo(object[0] != null ? object[0].toString() : null);
		dto.setPlannedLocation(object[2] != null ? object[2].toString() : null);
		dto.setVlsp(object[3] != null ? object[3].toString() : null);
		dto.setTransportMode(object[4] != null ? object[4].toString() : null);
		dto.setQuantity(object[5] != null ? object[5].toString() : null);
		dto.setCity(object[6] != null ? object[6].toString() : null);
		return dto;
	}).collect(Collectors.toList());
	
	logger.info("Exiting getCancelPlanByParams() Method: ");
	
	return CancelPlanlist;
}
	public List<String> loadCancelPlanNo(String authGroupValue)
			throws Exception {
	
	logger.info("Inside loadCancelPlan() Method: ");
		
	StringBuilder shipmentcancelQuery = new StringBuilder();
	List<String> shipmentPlanlist = new ArrayList<String>();

	shipmentcancelQuery.append(" SELECT  DISTINCT VDST02_SHIPMENTPLANNUMBER_C SHIPMENTPLANNUMBER FROM KVDST02_SHIPMENTPLAN ");
	shipmentcancelQuery.append(" INNER JOIN KVDST01_VIN VIN ON VIN.VDST01_SHIPMENTPLANNUMBER_C =VDST02_SHIPMENTPLANNUMBER_C");
	shipmentcancelQuery.append(" AND VIN.VDST01_LOADNUMBER_R = VDST02_LOADNUMBER_R AND VIN.VDST01_VDRNUMBER_C  IS NULL");
	shipmentcancelQuery.append(" AND NVL(VDST02_APPROVE_F,'N') ='Y' AND VDST02_STATUS_C ='C' AND NVL(VIN.VDST01_PLANNED_LOCATION,VIN.VDST01_LOCATION_C) IN('TEST1','TETS','HZVDC3','HZVDC4','TJVSC','CQ1VDC3','WHVSC2','WUHANVDC5','WATERBUFFERAREA','HZVDC2','HZVDC1','CQ1VDC45','ABCD','WUHU-INTRANSIT','CQET-PZC','CQ2VDC1','CQ1VDC2','CQ1VDC1','WHVSC1','CQPORT','Volvo External Yard','CQ-INTRANSIT','CQET-HUANSHAN','CQET-LIJIA','CQET-HC','CQET-BSQ','CQET-FDJ2','CQET-FDJ1','HZVDC','CQPORT-INTRANSIT','TJVSC1','XAVSC','TEST50','TEST111','INDIA','TJVSC1-INTRANSIT','BAYANNAOERCMPOUND','HBVDC2','HBVSC1','800','999','50','OOIO','CQET-ZJW','CQSTATION-INTRANSIT','CQSTATION','XAVSC-INTRANSIT','TJVSC-INTRANSIT','CQQ3','HBVDC1','HB-INTRANSIT','FORD-HBVDC1','FORD-HBVDC2')");
	shipmentcancelQuery.append(" AND SUBSTR(VDST02_SHIPMENTPLANNUMBER_C, 0 ,2) = SUBSTR('"+ authGroupValue +"', 0 ,2) ");
	shipmentcancelQuery.append(" UNION ");
	shipmentcancelQuery.append(" SELECT  DISTINCT VDST02_SHIPMENTPLANNUMBER_C SHIPMENTPLANNUMBER FROM KVDST02_SHIPMENTPLAN ");
	shipmentcancelQuery.append(" INNER JOIN KVDST01_VIN_CQ VIN ON VIN.VDST01_SHIPMENTPLANNUMBER_C =VDST02_SHIPMENTPLANNUMBER_C");
	shipmentcancelQuery.append(" AND VIN.VDST01_LOADNUMBER_R = VDST02_LOADNUMBER_R ");
	shipmentcancelQuery.append(" AND NVL(VDST02_APPROVE_F,'N') ='N' AND VDST02_STATUS_C ='D' AND NVL(VIN.VDST01_PLANNED_LOCATION,VIN.VDST01_LOCATION_C) IN('TEST1','TETS','HZVDC3','HZVDC4','TJVSC','CQ1VDC3','WHVSC2','WUHANVDC5','WATERBUFFERAREA','HZVDC2','HZVDC1','CQ1VDC45','ABCD','WUHU-INTRANSIT','CQET-PZC','CQ2VDC1','CQ1VDC2','CQ1VDC1','WHVSC1','CQPORT','Volvo External Yard','CQ-INTRANSIT','CQET-HUANSHAN','CQET-LIJIA','CQET-HC','CQET-BSQ','CQET-FDJ2','CQET-FDJ1','HZVDC','CQPORT-INTRANSIT','TJVSC1','XAVSC','TEST50','TEST111','INDIA','TJVSC1-INTRANSIT','BAYANNAOERCMPOUND','HBVDC2','HBVSC1','800','999','50','OOIO','CQET-ZJW','CQSTATION-INTRANSIT','CQSTATION','XAVSC-INTRANSIT','TJVSC-INTRANSIT','CQQ3','HBVDC1','HB-INTRANSIT','FORD-HBVDC1','FORD-HBVDC2')");
	shipmentcancelQuery.append(" AND SUBSTR(VDST02_SHIPMENTPLANNUMBER_C, 0 ,2) = SUBSTR('"+ authGroupValue +"', 0 ,2) ");
	shipmentcancelQuery.append(" ORDER BY SHIPMENTPLANNUMBER ");
	
	
	logger.info("get Cancel_SP Query: "+shipmentcancelQuery.toString());
	try 
	{
	Query query = em.createNativeQuery(shipmentcancelQuery.toString());

	List<Object[]> result = query.getResultList();
	
	for(Object obj :result)
		{
			if(obj!=null)
			{
				shipmentPlanlist.add(obj.toString());
	
			}		
		}
	}catch (Exception e) 
	{
	  e.printStackTrace();	
	}
	
	logger.info("Exiting getCancelPlanByParams() Method: ");
	
	return shipmentPlanlist;
}	

	


	

}
