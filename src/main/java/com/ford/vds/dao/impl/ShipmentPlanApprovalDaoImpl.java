package com.ford.vds.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

import com.ford.vds.config.ApplicationProperties;
import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.ShipmentPlanApprovalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
@Transactional
public class ShipmentPlanApprovalDaoImpl implements ShipmentPlanApprovalDao{
	
	@PersistenceContext
	private EntityManager em;
	private static Logger logger = LoggerFactory.getLogger(ShipmentPlanApprovalDaoImpl.class);

	@Override
	public List<String> getShipmentPlan(String authoritygroup,String userId) throws Exception {
		
		logger.info("Inside ShipmentPlanapprovalDaoImpl");
		String userRole = "C";
		StringBuffer approvalSetup = new StringBuffer();
		
		approvalSetup.append("SELECT VDSM34_APPROVALSETUP_ID,(select VDSM08_SCREEN_N from KVDSM08_SCREEN where VDSM08_SCREEN_D = VDSM34_SCREENID_D) ");
		approvalSetup.append("as VDSM34_SCREEN ,VDSM34_AUTHORIY_GROUP,VDSM34_SUPERVISOR_USER_D, VDSM34_SUPERVISOR_USER_F,VDSM34_MANAGER_USER_D, ");
		approvalSetup.append("VDSM34_MANAGER_USER_F  FROM KVDSM34_USER_SCREEN_APPROVER  where 1=1 ");
		approvalSetup.append("AND VDSM34_SCREENID_D ='"+ VDSConstant.SHIPMENTPLAN_SCREENID +"' AND VDSM34_AUTHORIY_GROUP = '"+ authoritygroup +"' ");
		
		logger.info("userid Query: "+approvalSetup.toString());
		Query sqlQuery = em.createNativeQuery(approvalSetup.toString());
		List<Object[]> approvalValue = sqlQuery.getResultList();
		for(Object[] value : approvalValue) {
			if(value[3] != null && value[5] != null) {
				if((value[3].toString().equalsIgnoreCase(userId) && value[5].toString().equalsIgnoreCase(userId)) 
						|| value[5].toString().equalsIgnoreCase(userId)) {
					userRole = "M";
				}else if(value[3].toString().equalsIgnoreCase(userId)){
					userRole = "S";
				}else {
					userRole = "C";
				}
			}
		}
		
		
		StringBuilder togetapprovalwaitingQuery = new StringBuilder();
		
		List<String> shipmentPlanlist = new ArrayList<String>();
		
//		togetapprovalwaitingQuery.append("SELECT DISTINCT SHIP.VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENTPLANNUMBER_C,TRUNC(VDST02_SHIPMENTPLANDATE_S) SHIPMENTPLANDATE,VDST02_CREATEUSER_C CREATEUSER "
//				+ " FROM KVDST02_SHIPMENTPLAN SHIP"
//				+ " JOIN KVDST01_VIN ON SHIP.VDST02_SHIPMENTPLANNUMBER_C = VDST01_SHIPMENTPLANNUMBER_C"
//				+ " AND SHIP.VDST02_LOADNUMBER_R        = VDST01_LOADNUMBER_R"
//				+ " WHERE 1=1"
//				+ " AND NVL(SHIP.VDST02_APPROVE_F,'N') IN ('S','M')"
//				+ " AND VDST02_SHIPMENTPLANNUMBER_C LIKE ('"+authoritygroup.substring(0,2)+"%')"
//				+ " AND TRUNC(VDST02_SHIPMENTPLANDATE_S) >"
//				+ " (SELECT VDSM03_VALUE_X FROM KVDSM03_LOV WHERE VDSM03_CATEGORY_N = 'LaunchDate')"
//				+ "  AND NVL(VDST02_INPROCESS_F,'N') IN "
//				+ (userRole.equals("S") ? " ('N') " : " ('N','S') ")
//				+ " GROUP BY SHIP.VDST02_SHIPMENTPLANNUMBER_C,VDST02_SHIPMENTPLANDATE_S,VDST02_CREATEUSER_C"
//				+ " ORDER BY SHIPMENTPLANDATE");
		
		togetapprovalwaitingQuery.append("SELECT DISTINCT VDST02_SHIPMENTPLANNUMBER_C,TRUNC(VDST02_SHIPMENTPLANDATE_S) SHIPMENTPLANDATE, ");
		togetapprovalwaitingQuery.append("VDST02_CREATEUSER_C CREATEUSER FROM KVDST02_SHIPMENTPLAN JOIN KVDST01_VIN ");
		togetapprovalwaitingQuery.append("ON VDST02_SHIPMENTPLANNUMBER_C = VDST01_SHIPMENTPLANNUMBER_C AND VDST02_LOADNUMBER_R = VDST01_LOADNUMBER_R ");
		togetapprovalwaitingQuery.append("WHERE NVL(VDST02_APPROVE_F,'N') IN ('S','M') AND VDST02_SHIPMENTPLANNUMBER_C LIKE ('"+authoritygroup.substring(0,2)+"%') ");
		togetapprovalwaitingQuery.append("AND NVL(VDST02_INPROCESS_F,'N') IN "+(userRole.equals("S") ? " ('N') " : " ('N','S') "));
		togetapprovalwaitingQuery.append("AND TRUNC(VDST02_SHIPMENTPLANDATE_S) > (SELECT VDSM03_VALUE_X FROM KVDSM03_LOV WHERE VDSM03_CATEGORY_N = 'LaunchDate') ");
		togetapprovalwaitingQuery.append("AND VDST02_SHIPMENTPLANNUMBER_C NOT IN (SELECT DISTINCT SHIP.VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENTPLANNUMBER_C ");
		togetapprovalwaitingQuery.append("FROM KVDST02_SHIPMENTPLAN SHIP JOIN KVDST01_VIN ON SHIP.VDST02_SHIPMENTPLANNUMBER_C = VDST01_SHIPMENTPLANNUMBER_C ");
		togetapprovalwaitingQuery.append("AND SHIP.VDST02_LOADNUMBER_R = VDST01_LOADNUMBER_R JOIN KVDST42_SHIPMENT_APPROVAL_DTLS DTL ON ");
		togetapprovalwaitingQuery.append("(DTL.VDST42_APPROVAL_KEY  = VDST01_SHIPMENTPLANNUMBER_C OR DTL.VDST42_APPROVAL_KEY = VDST01_VIN_C) ");
		togetapprovalwaitingQuery.append("WHERE 1 = 1 AND NVL(SHIP.VDST02_APPROVE_F,'N') IN ('S','M') AND VDST02_SHIPMENTPLANNUMBER_C LIKE ('"+authoritygroup.substring(0,2)+"%')");
		togetapprovalwaitingQuery.append("AND TRUNC(VDST02_SHIPMENTPLANDATE_S) > (SELECT VDSM03_VALUE_X FROM KVDSM03_LOV WHERE VDSM03_CATEGORY_N = 'LaunchDate' ) ");
		togetapprovalwaitingQuery.append("AND NVL(VDST02_INPROCESS_F,'N') IN ('N','S')) ORDER BY SHIPMENTPLANDATE ");
		
		logger.info("to getshipmentplan approval waiting query: "+approvalSetup.toString());
		
		Query query  = em.createNativeQuery(togetapprovalwaitingQuery.toString());
		
		List<Object[]> result = query.getResultList();
		
		for(Object[] obj :result)
		{
			if(obj[0].toString()!=null)
			{
				shipmentPlanlist.add(obj[0].toString()+"~"+userRole);
	
			}		
		}
		logger.info("Exit ShipmentPlanapprovalDaoImpl");
		return shipmentPlanlist;
	}

	



}
