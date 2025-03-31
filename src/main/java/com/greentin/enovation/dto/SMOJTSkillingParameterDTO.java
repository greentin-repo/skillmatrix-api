package com.greentin.enovation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greentin.enovation.model.skillMatrix.SMChecksheetParameter;
import com.greentin.enovation.model.skillMatrix.SMOJTSkillingAudit;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SMOJTSkillingParameterDTO {

	private long id;

	private String parameterValue;

	private long skillingAuditId;

	private long checksheetParameterId;

	private long skillingParameterId;

	private String status;

	private String comment;

	private String OEStatus;

	private List<SMOJTPlanDTO> cyclePlanList;

	private int cycleValue;

	private int cycleNo;

	private String parameterType;

	private String parameter;

	private String Label;

	private String actualValue;

	private String expectedValue;

	private List<SMOJTSkillingAuditDTO> skillList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public long getSkillingAuditId() {
		return skillingAuditId;
	}

	public void setSkillingAuditId(long skillingAuditId) {
		this.skillingAuditId = skillingAuditId;
	}

	public long getChecksheetParameterId() {
		return checksheetParameterId;
	}

	public void setChecksheetParameterId(long checksheetParameterId) {
		this.checksheetParameterId = checksheetParameterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOEStatus() {
		return OEStatus;
	}

	public void setOEStatus(String oEStatus) {
		OEStatus = oEStatus;
	}

	public List<SMOJTPlanDTO> getCyclePlanList() {
		return cyclePlanList;
	}

	public void setCyclePlanList(List<SMOJTPlanDTO> cyclePlanList) {
		this.cyclePlanList = cyclePlanList;
	}

	public int getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(int cycleValue) {
		this.cycleValue = cycleValue;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public long getSkillingParameterId() {
		return skillingParameterId;
	}

	public void setSkillingParameterId(long skillingParameterId) {
		this.skillingParameterId = skillingParameterId;
	}

	public int getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(int cycleNo) {
		this.cycleNo = cycleNo;
	}

	public String getActualValue() {
		return actualValue;
	}

	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}

	public String getExpectedValue() {
		return expectedValue;
	}

	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}

	public List<SMOJTSkillingAuditDTO> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SMOJTSkillingAuditDTO> skillList) {
		this.skillList = skillList;
	}

}
