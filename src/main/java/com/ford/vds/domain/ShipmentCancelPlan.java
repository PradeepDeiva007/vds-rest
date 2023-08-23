package com.ford.vds.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "KVDSI09_SHIPMENT_PLAN", schema = "CFMAVDS")
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "VDSI09_ROWNUM_R")),
		@AttributeOverride(name = "createUser", column = @Column(name = "VDSI09_CREATEDUSER")),
		@AttributeOverride(name = "createTime", column = @Column(name = "VDSI09_CREATEDDATE")),
		@AttributeOverride(name = "lastUpdateUser", column = @Column(name = "VDSI09_LASTUPDATEUSER")),
		@AttributeOverride(name = "lastUpdateTime", column = @Column(name = "VDSI09_UPDATEDDATE")) })

@Data
@EqualsAndHashCode(callSuper=false)

public class ShipmentCancelPlan extends Base {

	private static final long serialVersionUID = -8507009478040338723L;

	@Column(name = "VDST02_LOADNUMBER_R")
	private String loadNo;

	@Column(name = "VDSM17_CITY_NAME")
	private String city;

	@Column(name = "VIN.VDST01_PLANNED_LOCATION")
	private String plannedLocation;

	@Column(name = "VDST02_PLANNED_TRANSPORTMODE_C")
	private String tranportMode;

	@Column(name = "VDST02_PLANNED_VLSP_C")
	private String vlsp;

	@Column(name = "VDST02_SHIPMENTPLANNUMBER_C")
	private String shipmentNo;
	
	@Column(name = "VDST02_LOADQUANTITY_Q")
	private String quantity;
}
