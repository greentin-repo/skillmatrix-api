package com.greentin.enovation.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greentin.enovation.dto.SMAssessmentDTO;
import com.greentin.enovation.dto.SMOJTPlanDTO;
import com.greentin.enovation.dto.SMOJTRegisDTO;
import com.greentin.enovation.dto.SMOJTSkillingAuditDTO;
import com.greentin.enovation.dto.SMOJTSkillingDTO;
import com.greentin.enovation.dto.SMShiftDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillMatrixResponse {

	private String status;

	private Integer statusCode;

	private String reason;

	private boolean result;

	private long id;

	private List<HashMap<String, Object>> dataList;

	private HashMap<String, Object> data;

	private SkillMatrixRequest responseData;

	private HashMap<String, Object> questionList;

	private List<SMOJTSkillingDTO> actionList;

	private List<SMOJTPlanDTO> smActionList;

	private int totalCount;

	private SMAssessmentDTO Assessment;

	private SMOJTPlanDTO ojtPlan;

	private SMOJTSkillingAuditDTO actionDetails;

	List<SMShiftDTO> shiftList;

	List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> dayWiseAuditList;

	private String returnConstant;

	private List<HashMap<Object, List<SMOJTRegisDTO>>> dayWiseAuditsList;

	private List<SMOJTRegisDTO> regisList;

	private HashMap<String, Object> regiList;

	private List<Tuple> skillingList;

	private List<Map<String, Object>> skillList;

	private List<SkillMatrixRequest> errorList = new ArrayList<SkillMatrixRequest>();

	public SkillMatrixResponse() {
		super();
	}

	public SkillMatrixResponse(int statusCode, String status, boolean result, String reason) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.result = result;
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<HashMap<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<HashMap<String, Object>> dataList) {
		this.dataList = dataList;
	}

	public SkillMatrixRequest getResponseData() {
		return responseData;
	}

	public void setResponseData(SkillMatrixRequest responseData) {
		this.responseData = responseData;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public HashMap<String, Object> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(HashMap<String, Object> questionList) {
		this.questionList = questionList;
	}

	public List<SMOJTSkillingDTO> getActionList() {
		return actionList;
	}

	public void setActionList(List<SMOJTSkillingDTO> actionList) {
		this.actionList = actionList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public SMAssessmentDTO getAssessment() {
		return Assessment;
	}

	public void setAssessment(SMAssessmentDTO assessment) {
		Assessment = assessment;
	}

	public SMOJTPlanDTO getOjtPlan() {
		return ojtPlan;
	}

	public void setOjtPlan(SMOJTPlanDTO ojtPlan) {
		this.ojtPlan = ojtPlan;
	}

	public SMOJTSkillingAuditDTO getActionDetails() {
		return actionDetails;
	}

	public void setActionDetails(SMOJTSkillingAuditDTO actionDetails) {
		this.actionDetails = actionDetails;
	}

	public List<SMOJTPlanDTO> getSmActionList() {
		return smActionList;
	}

	public void setSmActionList(List<SMOJTPlanDTO> smActionList) {
		this.smActionList = smActionList;
	}

	public List<SMShiftDTO> getShiftList() {
		return shiftList;
	}

	public void setShiftList(List<SMShiftDTO> shiftList) {
		this.shiftList = shiftList;
	}

	public String getReturnConstant() {
		return returnConstant;
	}

	public void setReturnConstant(String returnConstant) {
		this.returnConstant = returnConstant;
	}

	public List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> getDayWiseAuditList() {
		return dayWiseAuditList;
	}

	public void setDayWiseAuditList(List<HashMap<Object, List<SMOJTSkillingAuditDTO>>> dayWiseAuditList) {
		this.dayWiseAuditList = dayWiseAuditList;
	}

	public List<HashMap<Object, List<SMOJTRegisDTO>>> getDayWiseAuditsList() {
		return dayWiseAuditsList;
	}

	public void setDayWiseAuditsList(List<HashMap<Object, List<SMOJTRegisDTO>>> dayWiseAuditsList) {
		this.dayWiseAuditsList = dayWiseAuditsList;
	}

	public List<SMOJTRegisDTO> getRegisList() {
		return regisList;
	}

	public void setRegisList(List<SMOJTRegisDTO> regisList) {
		this.regisList = regisList;
	}

	public HashMap<String, Object> getRegiList() {
		return regiList;
	}

	public void setRegiList(HashMap<String, Object> regiList) {
		this.regiList = regiList;
	}

	public List<Tuple> getSkillingList() {
		return skillingList;
	}

	public void setSkillingList(List<Tuple> skillingList) {
		this.skillingList = skillingList;
	}

	public List<Map<String, Object>> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<Map<String, Object>> skillList) {
		this.skillList = skillList;
	}

	public List<SkillMatrixRequest> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<SkillMatrixRequest> errorList) {
		this.errorList = errorList;
	}

}
