package com.ford.vds.webservice;

import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ford.vds.config.ApplicationProperties;

@Component
public class HttpUtil {

	@Value("${soap.mobilityshipmentplan.url}")
	private String reqURL;

	@Value("${soap.appproperties.url}")
	private String appPropertiesReqURL;
	
	private Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	@PostConstruct
	void init() {
		
		logger.info("Inside init() Method: ");
		
		StringEntity stringEntity;
		try {
			stringEntity = this.convertToXml(null);
			stringEntity.setChunked(true);
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/xml"));
			HttpEntity entity = this.processSoapRequest(stringEntity, "getAllContants");
			ApplicationProperties.loadProperties();		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting init() Method ");
	}

	public ProcessShipmentResponse callWebService(ShipmentPlanRequest request) throws IOException {
		
		logger.info("Inside callWebService() Method: ");
		
		StringEntity stringEntity = this.convertToXml(request);
		stringEntity.setChunked(true);
		stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/xml"));
		HttpEntity entity = this.processSoapRequest(stringEntity, "processShipment");
		ProcessShipmentResponse shipmentResponse = null;
		if (entity != null) {
			System.out.println(entity.toString());
			shipmentResponse = this.convertXmlToObj(EntityUtils.toString(entity));
		}
		
		logger.info("Exiting callWebService() Method ");
		
		return shipmentResponse;
	}

	private StringEntity convertToXml(ShipmentPlanRequest request) throws JsonProcessingException {
		
		logger.info("Inside convertToXml() Method: ");
		
		XmlMapper xmlMapper = new XmlMapper();
		StringBuilder requestBody = new StringBuilder();
		requestBody.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		requestBody.append(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:mobilityshipmentplan\"> ");
		requestBody.append("<soapenv:Body>");
		if (request != null) {
			String soapEnvBody = xmlMapper.writeValueAsString(request);
			soapEnvBody = soapEnvBody.replaceAll("ShipmentPlanRequest", "urn:processShipment");
			requestBody.append(soapEnvBody);
		}
		requestBody.append("</soapenv:Body></soapenv:Envelope>");
		
		logger.info("Exiting convertToXml() Method ");
		
		return new StringEntity(requestBody.toString(), "UTF-8");
	}

	private ProcessShipmentResponse convertXmlToObj(String xmldata)
			throws JsonMappingException, JsonProcessingException {
		
		logger.info("Inside convertXmlToObj() Method: ");
		
		XmlMapper xmlMapper = new XmlMapper();
		xmldata = xmldata.replaceAll("<return>", "");
		xmldata = xmldata.replaceAll("</return>", "");
		System.out.println(xmldata.toString());
		Envelope responseObj = xmlMapper.readValue(xmldata, Envelope.class);
		
		logger.info("Exiting convertXmlToObj() Method ");
		
		return responseObj.getBody().getProcessShipmentResponse();
	}

	private HttpEntity processSoapRequest(StringEntity stringEntity, String actionName)
			throws ClientProtocolException, IOException {
		
		logger.info("Inside processSoapRequest() Method: ");
		
		HttpEntity entity = null;
		try {
			HttpPost httpPost = new HttpPost(this.reqURL);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "text/xml");
			httpPost.addHeader("SOAPAction", actionName);
			HttpClient httpClient = HttpClients
		            .custom()
		            .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
		            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
		            .build();
			HttpResponse response = httpClient.execute(httpPost);
			entity = response.getEntity();
        } catch(Exception e) {
        	e.printStackTrace();
		}
		
		logger.info("Exiting processSoapRequest() Method: ");
		
		return entity;
	}
}
