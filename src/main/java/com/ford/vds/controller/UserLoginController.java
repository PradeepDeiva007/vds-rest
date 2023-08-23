package com.ford.vds.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import com.ford.vds.model.UserDTO;
import com.ford.vds.service.UserService;
import com.ford.vds.webservice.ProcessShipmentResponse;


@Controller()
@RequestMapping("userLogin/")
public class UserLoginController {

	@Autowired
	private UserService userService;
	static RestTemplate restTemplate = new RestTemplate();
	//private static final String GETACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wxcee4a837d6537e35&corpsecret=8tkOOlpzkC1k7-ZTWob4uJSVuMSNznK2PnAnCmBLMh8";
	private static final String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcee4a837d6537e35&redirect_uri=https%3A%2F%2Fvdsapp.changanford.cn%3A8443%2Fvds-lite-web%2F%23%2Flogin&response_type=code&scope=snsapi_base&state=&connect_redirect=1#wechat_redirect";
	//private static final String GET_USERINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=";
	private static final String GET_USER_ID = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=";
	
	@Value("${wechat.accessToken.url}")
	private String GETACCESS_TOKEN;
	
	private Logger logger = LoggerFactory.getLogger(UserLoginController.class);
			

	/* @GetMapping(path = "{userid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> handleSearchShipment(@PathVariable(required = false) String userid) {
		return new ResponseEntity<UserDTO>(userService.getUserById(userid), HttpStatus.OK);
	} */
	
	@GetMapping(path = "access", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleSearchShipment() {
		return new ResponseEntity<String>(userService.getAccessLoginScreen(), HttpStatus.OK);
	} 
	
	
	@GetMapping(path = "{code}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> togetUserInfo(@PathVariable(required = false) String code) {
		
		logger.info("Inside togetUserInfo() Method");
		
		/*
		 * try { HttpHeaders headers = new HttpHeaders();
		 * headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		 * 
		 * HttpEntity<UserDTO> entity = new HttpEntity<>(null, headers);
		 * ResponseEntity<UserDTO> result = restTemplate.exchange(GETACCESS_TOKEN,
		 * HttpMethod.GET, entity, UserDTO.class); System.out.println("~~~~~~~"+
		 * result.getBody()); System.out.println("------"+
		 * result.getBody().getAccess_token());
		 * 
		 * } catch(Exception e) { e.printStackTrace(); }
		 */
		String employeeId = "";
		try {
			String accessToken = togetAccessToken();
			String accessCode =  code;
		    //togetAccessCode().toString().substring(togetAccessCode().toString().indexOf("?code=") + 1, togetAccessCode().toString().indexOf("&state="));
			logger.info("ACCESS_TOKEN: -------> "+accessToken);
			logger.info("ACCESS_CODE: --------> "+code);
			employeeId = togetUserID(accessToken,accessCode);
			employeeId = employeeId != null ? employeeId : "ZWANG104";
		} catch(Exception e) {
			e.printStackTrace();
		}
			
		logger.info("Exiting togetUserInfo() Method");
		
		return employeeId != null ? new ResponseEntity<UserDTO>(userService.getEmployeeID(employeeId), HttpStatus.OK) : null;
	}


	public String togetUserID(String accessToken, String code) {
		
		logger.info("Inside togetUserID() Method");
		
		ResponseEntity<String> result = null;
		String userId = "";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add("Authorization", "Bearer " + accessToken);
			//String userURL = GET_USERINFO+accessToken+"&code="+code;
			logger.info("ACCESS_TOKEN:-------> "+accessToken);
			logger.info("ACCESS_CODE:--------> "+code);
			String GETUSERID = GET_USER_ID+accessToken+"&code="+code;
			logger.info("URL_TO_GET_USERID: "+GETUSERID);
			
			HttpEntity<String> entity = new HttpEntity<>(null,headers);
			result = restTemplate.exchange(GETUSERID , HttpMethod.POST, entity, String.class);
			logger.info("User_Result:------> "+ result);
			String userDTO = result.getBody();
			logger.info("User_Result_Body:------> "+ userDTO);
			userId = userDTO.substring(10,userDTO.indexOf(",",10)).replace("\"","");
			logger.info("Employee_ID:-------> "+userId);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting togetUserID() Method");
		
		return userId;
	}
	
	public String togetAccessToken() {
		
		logger.info("Inside togetAccessToken Method()");
		
		ResponseEntity<UserDTO> result = null;
		try {
			logger.info("Access_Token URL-------> "+ GETACCESS_TOKEN);
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			
			HttpEntity<UserDTO> entity = new HttpEntity<>(null, headers);
			result = restTemplate.exchange(GETACCESS_TOKEN, HttpMethod.GET, entity, UserDTO.class);
			logger.info("AccessToken_Result------> "+ result);
			logger.info("AccessToken_Body------> "+ result.getBody());
			logger.info("Access_Token------> "+ result.getBody().getAccess_token());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting togetAccessToken Method()");
		
		return result.getBody().getAccess_token();
	}
	
	public ResponseEntity<String> togetAccessCode() {
		
		logger.info("Inside togetAccessCode() Method");
		
		ResponseEntity<String> resultCode = null;
		try {
			HttpHeaders headersCode = new HttpHeaders();
			headersCode.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
			HttpEntity<String> entityCode = new HttpEntity<>("parameters", headersCode);
			resultCode = (restTemplate.exchange(GET_CODE, HttpMethod.GET, entityCode, String.class));
			System.out.println(resultCode);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Exiting togetAccessCode() Method");
		
		return resultCode;
	}
	
	@PostMapping(path = "changeAuthority", params = {"id","authGroup"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessShipmentResponse> changeAuthorityGroup(@RequestParam String authGroup, 
			@RequestParam(value="id") String userId) {
		
		logger.info("Called SuccessFully changeAuthorityGroup() Controller: ");
		
		return new ResponseEntity <ProcessShipmentResponse>(userService.updateAuthorityGroup(authGroup,userId), HttpStatus.CREATED);
	}
	
}
