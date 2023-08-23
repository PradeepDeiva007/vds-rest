package com.ford.vds.service;

import com.ford.vds.model.SubcriptionDTO;

public interface NotificationService {

	Boolean subscribeUser(SubcriptionDTO dto);
	
	Boolean unsubscribeUser(SubcriptionDTO dto);
	
	Boolean sendNotifications();
	
	Boolean sendShipmentPlanNotification(String process, String[] shipmentPlanNo);
}
