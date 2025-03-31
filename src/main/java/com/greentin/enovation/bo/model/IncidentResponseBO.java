package com.greentin.enovation.bo.model;

import java.util.List;

public class IncidentResponseBO {
	
	private String status;
	private boolean result;
	private Integer statusCode;
	private String reason;
    private String returnConstant;
	List<IncidentInfoBO> rolewiseIncidentStatusList;
	List<IncidentInfoBO> employeeWiseMonthlyIncidentCount;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<IncidentInfoBO> getRolewiseIncidentStatusList() {
		return rolewiseIncidentStatusList;
	}

	public void setRolewiseIncidentStatusList(List<IncidentInfoBO> rolewiseIncidentStatusList) {
		this.rolewiseIncidentStatusList = rolewiseIncidentStatusList;
	}

	public List<IncidentInfoBO> getEmployeeWiseMonthlyIncidentCount() {
		return employeeWiseMonthlyIncidentCount;
	}

	public void setEmployeeWiseMonthlyIncidentCount(List<IncidentInfoBO> employeeWiseMonthlyIncidentCount) {
		this.employeeWiseMonthlyIncidentCount = employeeWiseMonthlyIncidentCount;
	}

	public String getReturnConstant() {
		return returnConstant;
	}

	public void setReturnConstant(String returnConstant) {
		this.returnConstant = returnConstant;
	}
	
}
