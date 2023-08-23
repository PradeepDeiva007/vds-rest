package com.ford.vds.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ford.vds.config.VDSConstant;
import com.ford.vds.dao.CancelShipmentPlanApprovalDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
@Transactional
public class CancelShipmentPlanApprovalDaoImpl implements CancelShipmentPlanApprovalDao {

	@PersistenceContext
	private EntityManager em;
	
	private static Logger logger = LoggerFactory.getLogger(CancelShipmentPlanApprovalDaoImpl.class);
	
	@Override
	public List<String> getShipmentPlan(String authoritygroup,String userId) throws Exception {
		
		logger.info("Inside getcancelShipmentPlanapprovalDaoImpl");
		
		String userRole = "C";
		StringBuffer approvalSetup = new StringBuffer();
		
		approvalSetup.append("SELECT VDSM34_APPROVALSETUP_ID,(select VDSM08_SCREEN_N from KVDSM08_SCREEN where VDSM08_SCREEN_D = VDSM34_SCREENID_D) ");
		approvalSetup.append("as VDSM34_SCREEN ,VDSM34_AUTHORIY_GROUP,VDSM34_SUPERVISOR_USER_D, VDSM34_SUPERVISOR_USER_F,VDSM34_MANAGER_USER_D, ");
		approvalSetup.append("VDSM34_MANAGER_USER_F  FROM KVDSM34_USER_SCREEN_APPROVER  where 1=1 ");
		approvalSetup.append("AND VDSM34_SCREENID_D ='"+ VDSConstant.SHIPMENTPLAN_SCREENID +"' AND VDSM34_AUTHORIY_GROUP = '"+ authoritygroup +"' ");
		
		logger.info("User Approval Query: "+approvalSetup.toString());
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
		
		togetapprovalwaitingQuery.append("SELECT DISTINCT DTL.VDST42_APPROVAL_KEY KEY,DTL.VDST42_APPROVAL_PROCESS,DTL.VDST42_APPROVAL_VALUE AS VALUE FROM KVDST42_SHIPMENT_APPROVAL_DTLS DTL "
				+ " JOIN kvdst01_vin VIN ON (DTL.VDST42_APPROVAL_KEY= VDST01_SHIPMENTPLANNUMBER_C or  DTL.VDST42_APPROVAL_KEY = VDST01_VIN_C)"
				+ " AND VDST42_APPROVAL_PROCESS IN ('C','A','R','T')"
				+ " JOIN KVDST02_SHIPMENTPLAN PLN "
				+ " ON ( VDST02_SHIPMENTPLANNUMBER_C =VDST01_SHIPMENTPLANNUMBER_C"
				+ " AND VDST01_LOADNUMBER_R          =VDST02_LOADNUMBER_R)"
				+ " AND PLN.VDST02_APPROVE_F IN "
				+ (userRole.equals("S") ? " ('S') " : " ('S','M') ")
				+ " AND VDST02_SHIPMENTPLANNUMBER_C LIKE '"+ authoritygroup.substring(0,2) +"%'"
				);
		
		logger.info("Cancel Shipmentplan Approval Waiting Qry: "+togetapprovalwaitingQuery.toString());
		Query query  = em.createNativeQuery(togetapprovalwaitingQuery.toString());
		
		List<Object[]> result = query.getResultList();
		
		for(Object[] obj :result)
		{
			if(obj[0]!=null)
			{
				if(obj[1].toString().contains("C")) {
				shipmentPlanlist.add(obj[0].toString()+"~"+userRole);
				}else {
					String seqNo = obj[2].toString().split("~")[0];
					shipmentPlanlist.add(seqNo+"~"+userRole);
				}
	
			}		
		}	
		
		logger.info("Exit getcancelShipmentPlanapprovalDaoImpl");
		return shipmentPlanlist;
	}

	

}
