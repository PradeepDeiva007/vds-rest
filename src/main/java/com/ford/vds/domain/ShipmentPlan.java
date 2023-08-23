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
public class ShipmentPlan extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8507009478040338723L;

	@Column(name = "VDSI09_LOADNUMBER")
	private String loadNo;

	@Column(name = "VDSI09_CITY")
	private String city;

	@Column(name = "VDSI09_PLANNED_LOCATION")
	private String plannedLocation;

	@Column(name = "VDSI09_TRANSPORT_MODE")
	private String tranportMode;

	@Column(name = "VDSI09_VLSP")
	private String vlsp;

	@Column(name = "VDSI09_SHIPMENTPLAN_NUMBER")
	private String shipmentNo;

}
