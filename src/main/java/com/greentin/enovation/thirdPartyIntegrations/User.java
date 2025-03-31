/**
 * 
 */
package com.greentin.enovation.thirdPartyIntegrations;

import java.util.List;

/**
 * @author Vinay B
 * @date May 26, 2020 7:31:44 PM
 */
public class User {

	private String username;

	private String password;

	private String serviceName;

	private User auxiliaryParamters;

	private List<User> serviceParameters;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public User getAuxiliaryParamters() {
		return auxiliaryParamters;
	}

	public void setAuxiliaryParamters(User auxiliaryParamters) {
		this.auxiliaryParamters = auxiliaryParamters;
	}

	public List<User> getServiceParameters() {
		return serviceParameters;
	}

	public void setServiceParameters(List<User> serviceParameters) {
		this.serviceParameters = serviceParameters;
	}

}
