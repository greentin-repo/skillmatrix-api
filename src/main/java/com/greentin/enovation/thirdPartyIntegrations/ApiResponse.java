/**
 * 
 */
package com.greentin.enovation.thirdPartyIntegrations;

import java.util.List;

/**
 * @author Vinay B
 * @date May 27, 2020 2:08:40 PM
 */
public class ApiResponse {

	private String Success;

	private String Message;

	private List<ResponseData> ResponseData;

	public String getSuccess() {
		return Success;
	}

	public void setSuccess(String success) {
		Success = success;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public List<ResponseData> getResponseData() {
		return ResponseData;
	}

	public void setResponseData(List<ResponseData> responseData) {
		ResponseData = responseData;
	}

}
