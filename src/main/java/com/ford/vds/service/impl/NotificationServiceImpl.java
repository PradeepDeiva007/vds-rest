package com.ford.vds.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ford.vds.dao.ShipmentPlanGenerationDao;
import com.ford.vds.domain.UserEndpoints;
import com.ford.vds.model.SubcriptionDTO;
import com.ford.vds.repository.UserEndPointRepository;
import com.ford.vds.service.NotificationService;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.Subscription.Keys;



@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Value("${notification.public.key}")
	private String publicKey;

	@Value("${notification.private.key}")
	private String privateKey;

	@Autowired
	private PushService pushService;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserEndPointRepository userEndpointRepository;
	
	@Autowired
	private ShipmentPlanGenerationDao shipmentPlanDao;
	
	private Boolean status = false;
	
	private Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@PostConstruct
	private void init() throws GeneralSecurityException {
		
		logger.info("Inside init() Method: "); 
		
		Security.addProvider(new BouncyCastleProvider());
		pushService = new PushService(publicKey, privateKey);
		
		logger.info("Exiting init() Method: ");
	}

	@Override
	public Boolean subscribeUser(SubcriptionDTO dto) {
		
		logger.info("Inside subscribeUser() Method: ");
		
		UserEndpoints endpointObj = new UserEndpoints();
		modelMapper.map(dto, endpointObj);
		logger.info("UserEndPoind_Obj: "+endpointObj);
		boolean validUser = true;
		List<UserEndpoints> user = userEndpointRepository.findByEndPoint(endpointObj.getEndpoint());
		for (UserEndpoints endPoint : user) {
			if (endPoint.getUserId().equals(endpointObj.getUserId())
					&& endPoint.getEndpoint().equals(endpointObj.getEndpoint())) {
				validUser = false;
			}
		}
		if (validUser) {
			userEndpointRepository.save(endpointObj);
			return true;
		}
		
		logger.info("Exiting subscribeUser() Method: ");

		return false;
	}

	@Override
	public Boolean unsubscribeUser(SubcriptionDTO dto) {
		return true;
	}

	public void sendNotification(Subscription subscription, String messageJson) {
		
		logger.info("Inside sendNotification() Method: ");
		
		try {
			pushService.send(new Notification(subscription, messageJson));
		} catch (GeneralSecurityException | IOException | ExecutionException | InterruptedException | JoseException e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting sendNotification() Method: ");
	}

	@Override
	public Boolean sendNotifications() {
		
		logger.info("Inside sendNotifications() Method: ");
		
		String json = "{ \"notification\": {\"title\": \"message title\", \"body\": \"message body %s\"} }";
		List<UserEndpoints> endpointList = userEndpointRepository.findAll();
		endpointList.forEach(endpoint -> {
			Subscription subscription = new Subscription(endpoint.getEndpoint(),
					new Keys(endpoint.getP256dhkey(), endpoint.getAuthKey()));
			sendNotification(subscription, String.format(json, LocalTime.now()));
		});
		
		logger.info("Exiting sendNotifications() Method: ");
		
		return true;
	}

	public Boolean sendShipmentPlanMsg(String[] address, String shipmentPlanNo, String message) {
		
		logger.info("Inside sendShipmentPlanMsg() Method: ");
		
		status = false;
		String json = "{ \"notification\": {\"title\":\"" + message + "\", \"body\": \"Shipment Plan No: "
				+ shipmentPlanNo + " Submitted time: %s\"} }";
		logger.info("Notification_MSG:-----> "+json);
		List<UserEndpoints> endpointList = userEndpointRepository.findConfirmAddress(address);
		endpointList.forEach(endpoint -> {
			Subscription subscription = new Subscription(endpoint.getEndpoint(),
					new Keys(endpoint.getP256dhkey(), endpoint.getAuthKey()));
			sendNotification(subscription, String.format(json, LocalTime.now()));
			status = true;
		});
		
		logger.info("Exiting sendShipmentPlanMsg() Method: ");
		
		return status;
	}
	@Override
	public Boolean sendShipmentPlanNotification(String process, String[] shipmentPlanNo) {
		
		logger.info("Inside sendShipmentPlanNotification() Method: "); 
		
		String message =null;
		if(process.equals("Confirm_Plan") || process.equals("Cancel_plan")) {
			String[] userAddress = shipmentPlanDao.getUserAddress(shipmentPlanNo[0]);
			 message = "Shipment Plan waiting for approval";
			return sendShipmentPlanMsg(userAddress,shipmentPlanNo[0],message);
		}else if(process.equals("approval") || process.equals("reject") || process.equals("cancelapproval") || process.equals("cancelreject")) {	
	        for(String sp:shipmentPlanNo) 
	        {
			String[] userAddress = shipmentPlanDao.getUserAddress(sp);
			if(process.equals("approval"))
			   message = "Shipment Plan approved Successfully";
			if(process.equals("reject"))
			  message = "Shipment Plan rejected Successfully";	
			if(process.equals("cancelapproval"))
			  message = "Cancelled Shipment Plan approved Successfully";
			if(process.equals("cancelreject"))
			message = "Cancelled Shipment Plan rejected Successfully";
			
			return sendShipmentPlanMsg(userAddress,sp,message);	
	        }
		}
		
		logger.info("Exiting sendShipmentPlanNotification() Method: ");
		
		return false;
	}

	

}
