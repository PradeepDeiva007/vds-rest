package com.ford.vds.dao.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ford.vds.dao.ShipmentDao;
import com.ford.vds.model.ShipmentPlanDTO;

@Repository
@Transactional
public class ShipmentDaoImpl implements ShipmentDao {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<ShipmentPlanDTO> getShipmentPlanByParams(List<String> loadId, List<String> city, List<String> location,
			List<String> transportMode, List<String> vlsp) throws Exception {
		String sb = "SELECT * FROM (SELECT sp.vdsi09_loadnumber, sp.vdsi09_city,sp.vdsi09_planned_location,sp.vdsi09_transport_mode,"
				+ "sp.vdsi09_vlsp,sp.vdsi09_shipmentplan_number FROM KVDSI09_SHIPMENT_PLAN sp)" + "WHERE ROWNUM <= 10";
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("SELECT SPLAN.VDST02_SHIPMENTPLANNUMBER_C AS SHIPMENTPLANNUMBER_C," + "VDST02_LOADNUMBER_R,"
				+ "VDST01_PLANNED_LOCATION," + "    VDST02_PLANNED_VLSP_C VLSP_NAME," + "    CASE"
				+ "      WHEN UPPER(TMODE.VDSM05_STAND_TRANS_MODE) = 'COMBINED MODE'"
				+ "      THEN SUBSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER,1,INSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER,'~')-1)"
				+ "      ELSE SPLAN.VDST02_PLANNED_TRANSPORTMODE_C" + "    END AS TRANSPORT_MODE,"
				+ "    SUM(SPLAN.VDST02_LOADQUANTITY_Q)      AS PLANNED_CAPACITY,"
				+ "    (SELECT VDSM17_CITY_CHINESE_NAME" + "    FROM KVDSM17_CITY"
				+ "    WHERE VDSM17_CITY_NAME=L.SOURCE_CITY" + "    ) SOURCE_CITY_CHINESE,"
				+ "    L.SOURCE_CITY SOURCE_CITY" + "  FROM KVDST02_SHIPMENTPLAN SPLAN"
				+ "  JOIN KVDSM05_TRANSPORTMODE TMODE"
				+ "  ON TMODE.VDSM05_TRANSPORTMODE_X = SPLAN.VDST02_PLANNED_TRANSPORTMODE_C"
				+ "  JOIN KVDSM35_TRANSPORT_AUTH TAUTH"
				+ "  ON TAUTH.VDSM05_TRANSPORTMODE_X         = TMODE.VDSM05_TRANSPORTMODE_X"
				+ "  AND UPPER(TAUTH.VDSM35_AUTHORITY_GROUP) = 'CQTEAM'" + "  JOIN"
				+ "    (SELECT DISTINCT VDST01_SHIPMENTPLANNUMBER_C," + "      VDST01_LOADNUMBER_R,"
				+ "      VDST01_PLANNED_LOCATION" + "    FROM KVDST01_VIN_CQ" + "    ) VIN"
				+ "  ON VIN.VDST01_SHIPMENTPLANNUMBER_C = SPLAN.VDST02_SHIPMENTPLANNUMBER_C"
				+ "  AND VIN.VDST01_LOADNUMBER_R        = SPLAN.VDST02_LOADNUMBER_R" + "  JOIN"
				+ "    (SELECT DISTINCT VDST06_SHIPMENTPLANNUMBER_C SHIP_NUMBER,"
				+ "      VDST06_LOADNUMBER_R LOAD_NUMBER," + "      VDST06_SOURCECITY_C SOURCE_CITY"
				+ "    FROM KVDST06_LOADDETAILS LOAD" + "    ) L"
				+ "  ON L.SHIP_NUMBER  = SPLAN.VDST02_SHIPMENTPLANNUMBER_C"
				+ "  AND L.LOAD_NUMBER = SPLAN.VDST02_LOADNUMBER_R" + "  LEFT OUTER JOIN"
				+ "    (SELECT KVDSM15_VLSP.VDSM15_VLSPNAME AS VLSP_NAME," + "      VDSM05_TRANSPORTMODE_X VLSP_TMODE,"
				+ "      NVL(VDSM16_DAILY_CAPACITY,0) - NVL(VDSM16_HARDCONSUMED_CAPACITY,0) VLSP_CAPACITY,"
				+ "      VLSP_TMODE.VDSM15_VLSP_ID AS VLSP_ID," + "      VDSM16_SOURCE_CITY SOURCE_CITY"
				+ "    FROM KVDSM16_VLSP_TRANSPORT VLSP_TMODE" + "    JOIN KVDSM15_VLSP"
				+ "    ON KVDSM15_VLSP.VDSM15_VLSP_ID = VLSP_TMODE.VDSM15_VLSP_ID"
				+ "    WHERE VDSM16_ACTIVE            = 'Y'"
				+ "    ) E ON E.VLSP_NAME             = SPLAN.VDST02_PLANNED_VLSP_C"
				+ "  AND E.VLSP_TMODE                 = (" + "    CASE"
				+ "      WHEN UPPER(TMODE.VDSM05_STAND_TRANS_MODE) = 'COMBINED MODE'"
				+ "      THEN SUBSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER,1,INSTR(TAUTH.VDSM05_COMBINEDMODE_ORDER,'~')-1)"
				+ "      ELSE SPLAN.VDST02_PLANNED_TRANSPORTMODE_C" + "    END)" + "  AND E.SOURCE_CITY = L.SOURCE_CITY"
				+ "  LEFT OUTER JOIN" + "    (SELECT VDST02_PLANNED_VLSP_C VLSP_NAME,"
				+ "      VDST02_PLANNED_TRANSPORTMODE_C TMODE," + "      SUM(SHIP.VDST02_EXCEEDEDLOADQTY_Q) EXCEEDED,"
				+ "      L.SOURCE_CITY" + "    FROM KVDST02_SHIPMENTPLAN SHIP" + "    JOIN"
				+ "      (SELECT DISTINCT VDST06_SHIPMENTPLANNUMBER_C SHIP_NUMBER,"
				+ "        VDST06_LOADNUMBER_R LOAD_NUMBER," + "        VDST06_SOURCECITY_C SOURCE_CITY"
				+ "      FROM KVDST06_LOADDETAILS LOAD" + "      ) L"
				+ "    ON L.SHIP_NUMBER                   = SHIP.VDST02_SHIPMENTPLANNUMBER_C"
				+ "    AND L.LOAD_NUMBER                  = SHIP.VDST02_LOADNUMBER_R"
				+ "    WHERE SHIP.VDST02_STATUS_C         = 'D'" + "    AND SHIP.VDST02_APPROVE_F         IS NULL"
				+ "    AND SHIP.VDST02_ISEXCEEDEDCAPACITY = 'Y'" + "    GROUP BY VDST02_PLANNED_VLSP_C,"
				+ "      VDST02_PLANNED_TRANSPORTMODE_C," + "      L.SOURCE_CITY"
				+ "    ) S ON S.VLSP_NAME = SPLAN.VDST02_PLANNED_VLSP_C"
				+ "  AND S.TMODE          = SPLAN.VDST02_PLANNED_TRANSPORTMODE_C"
				+ "  AND S.SOURCE_CITY    = L.SOURCE_CITY" + "  JOIN KVDSM15_VLSP VLSP"
				+ "  ON VDST02_PLANNED_VLSP_C           = VLSP.VDSM15_VLSPNAME"
				+ "  WHERE SPLAN.VDST02_STATUS_C        = 'D'" + "  AND NVL(Splan.VDST02_APPROVE_F,'N')= 'N'"
				+ "  GROUP BY SPLAN.VDST02_SHIPMENTPLANNUMBER_C ," + "    E.VLSP_ID," + "    VDST02_PLANNED_VLSP_C,"
				+ "    TMODE.VDSM05_STAND_TRANS_MODE," + "    VDST02_PLANNED_TRANSPORTMODE_C,"
				+ "    TAUTH.VDSM05_COMBINEDMODE_ORDER,"
				+ "    L.SOURCE_CITY,VDST01_PLANNED_LOCATION,VDST02_LOADNUMBER_R ORDER BY VDST02_PLANNED_VLSP_C,"
				+ "    VDST02_PLANNED_TRANSPORTMODE_C");
		Query query = em.createNativeQuery(sbQuery.toString());
//		query.setParameter("loadId", loadId);
		List<Object[]> result = query.getResultList();
		List<ShipmentPlanDTO> shipmentPlanlist = result.stream().filter(Objects::nonNull).map(object -> {
			ShipmentPlanDTO dto = new ShipmentPlanDTO();
			dto.setLoadNo(object[0] != null ? object[0].toString() : null);
			dto.setCity(object[1] != null ? object[1].toString() : null);
			dto.setPlannedLocation(object[2] != null ? object[2].toString() : null);
			dto.setShipmentNo(object[5] != null ? object[5].toString() : null);
			dto.setTransportMode(object[3] != null ? object[3].toString() : null);
			dto.setVlsp(object[4] != null ? object[4].toString() : null);
			return dto;
		}).collect(Collectors.toList());
		return shipmentPlanlist;
	}

}
