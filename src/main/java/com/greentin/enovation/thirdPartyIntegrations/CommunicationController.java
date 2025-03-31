/**
 * 
 */
package com.greentin.enovation.thirdPartyIntegrations;

import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

/**
 * @author Vinay B
 * @date May 26, 2020 4:57:06 PM
 */
@CrossOrigin
@RestController
// @RequestMapping(value = "/communicationController")
public class CommunicationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationController.class);

	// Both RestTemplate and URI instances can be cached
	private static final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private ExternalApiTriggers ept;

	public String authenticateUser() throws JSONException {
		String uri = "http://10.37.61.247/AscentIS/Api/AuthenticateUser";
		JSONObject user = new JSONObject();
		user.put("username", "Ascentis");
		user.put("password", "ascent123$");
		System.out.println("Json ==> " + user);

		LOGGER.info(" Inside authenticate user | request url " + uri);
		LOGGER.info(" Inside authenticate user | request body " + user);

		// String token = restTemplate.postForObject(uri, user, String.class);

		String token = "N7FO/9ZNy7kvKP4XO3PYaBmKWszWW6GzgSRHAu7UhMtRkTOOfG4OrBKbclE8G1+v0D372+BC8QfH7QWTNaSpITqAZF3QVDe1mHqVsWdsy+vQ0ajUiuIJDV+MLsaL/uD//gZbLLjq8ZtCoTvlXc3wGe21kUYLjGCzl0/7q/tcEOc5XA9TDXIixJmu5+bMQYg9E0WXTxUZ/1g3duqBKmiHDg==";

		return token;

	}

	// @PostMapping(value = "connector/externalApiTesting")
	public ApiResponse getEmployeeDetails() throws JSONException {

		String token = authenticateUser();
		ApiResponse res = null;
		if (token != null) {

			System.out.println("Token is not null ==> " + token);

			LOGGER.info(" Inside get employee response | token details " + token);

			String uri = "http://prod.greentinsolutions.com/GGRLocService/test/getlanguagelistByPost";

			LOGGER.info(" Inside get employee response | request url " + uri);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			// headers.set("Auth_Token", token);

			HttpEntity<Object> request = new HttpEntity<>(headers);

			JSONObject obj = new JSONObject();
			JSONArray serviceParam = new JSONArray();

			JSONObject body = new JSONObject();
			body.put("serviceName", "GETEMPLOYEES");
			body.put("serviceParameters", serviceParam);
			body.put("auxiliaryParamters", obj);

			request = new HttpEntity<>(body.toString(), headers);

			System.out.println("Json ==> " + body);

			LOGGER.info(" Inside get employee response | request body " + body);

			String apiRes = restTemplate.postForObject(uri, request, String.class);

			// ResponseEntity<String> apiRes = restTemplate.exchange(uri, HttpMethod.POST,
			// request, ApiResponse.class);

			Gson gson = new Gson();
			String json = gson.toJson(apiRes);
			System.out.println("Response from api ==> " + json);

			Gson g = new Gson();
			res = g.fromJson(apiRes, ApiResponse.class);
		} else {
			LOGGER.info(" Inside get employee response | token details not received ");
			System.out.println("");
		}
		return res;

		/** Method 2 - calling external api by restTemplate exchange method */

		/*
		 * // Map<String, String> body = new HashMap<String, String>();
		 * 
		 * JSONObject body = new JSONObject(); body.put("serviceName", "GETEMPLOYEES");
		 * body.put("serviceParameters", serviceParam); body.put("auxiliaryParamters",
		 * obj);
		 * 
		 * 
		 * User auxObj = new User(); List<User> serList = new ArrayList<User>(); User u
		 * = new User(); u.setServiceName("GETEMPLOYEES");
		 * u.setAuxiliaryParamters(auxObj); u.setServiceParameters(serList);
		 * System.out.println("Json ==> " + u);
		 * 
		 * Gson gson1 = new Gson(); String json1 = gson1.toJson(u);
		 * System.out.println("Request Body ==> " + json1);
		 * 
		 * request = new HttpEntity<>(u, headers);
		 * 
		 * // String uri = "http://10.37.61.247/AscentIS/Api/ExecService";
		 * 
		 * System.out.println("Request Details ==> " + request);
		 * 
		 * // ResponseEntity<MasterResponse> response = restTemplate.postForEntity(uri,
		 * // request, MasterResponse.class);
		 * 
		 * 
		 * ResponseEntity<MasterResponse> response = restTemplate.exchange(uri,
		 * HttpMethod.POST, request, MasterResponse.class);
		 * 
		 * System.out.println("Result - status (" + response.getStatusCode() +
		 * ") has body: " + response.hasBody());
		 * 
		 * Gson gson = new Gson(); String json = gson.toJson(response);
		 * System.out.println("Response from api ==> " + json);
		 * 
		 * return response.getBody();
		 */

	}

	@GetMapping(value = "test/daiplAddUpdateEmployee")
	public ApiResponse daiplAddUpdateEmployee() throws JSONException {
		LOGGER.info("# Inside daiplAddUpdateEmployee");
		return ept.getEmployeeDetails();
	}

}
