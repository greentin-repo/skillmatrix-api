package com.greentin.enovation.dto;

public class TagDto {
	private String tagNumber;

	private String orgAlies;

	private String createdDate;

	private String deptName;

	private String detector;

	private String defectName;

	private String linkedLoss;

	private String targetDate;

	private String closedByName;

	private String closedByDate;

	private String eqptName;

	private String detectionDate;

	private int tagTypeId;

	public TagDto() {
		super();
	}

	public TagDto(String tagNumber, String orgAlies, String createdDate, String deptName, String detector,
			String defectName, String linkedLoss, String targetDate, String closedByName, String closedByDate,
			String eqptName, String detectionDate, int tagTypeId) {
		super();
		this.tagNumber = tagNumber;
		this.orgAlies = orgAlies;
		this.createdDate = createdDate;
		this.deptName = deptName;
		this.detector = detector;
		this.defectName = defectName;
		this.linkedLoss = linkedLoss;
		this.targetDate = targetDate;
		this.closedByName = closedByName;
		this.closedByDate = closedByDate;
		this.eqptName = eqptName;
		this.detectionDate = detectionDate;
		this.tagTypeId = tagTypeId;
	}

	public String getTagNumber() {
		return tagNumber;
	}

	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	public String getOrgAlies() {
		return orgAlies;
	}

	public void setOrgAlies(String orgAlies) {
		this.orgAlies = orgAlies;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDetector() {
		return detector;
	}

	public void setDetector(String detector) {
		this.detector = detector;
	}

	public String getDefectName() {
		return defectName;
	}

	public void setDefectName(String defectName) {
		this.defectName = defectName;
	}

	public String getLinkedLoss() {
		return linkedLoss;
	}

	public void setLinkedLoss(String linkedLoss) {
		this.linkedLoss = linkedLoss;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public String getClosedByName() {
		return closedByName;
	}

	public void setClosedByName(String closedByName) {
		this.closedByName = closedByName;
	}

	public String getClosedByDate() {
		return closedByDate;
	}

	public void setClosedByDate(String closedByDate) {
		this.closedByDate = closedByDate;
	}

	public String getEqptName() {
		return eqptName;
	}

	public void setEqptName(String eqptName) {
		this.eqptName = eqptName;
	}

	public String getDetectionDate() {
		return detectionDate;
	}

	public void setDetectionDate(String detectionDate) {
		this.detectionDate = detectionDate;
	}

	public int getTagTypeId() {
		return tagTypeId;
	}

	public void setTagTypeId(int tagTypeId) {
		this.tagTypeId = tagTypeId;
	}








}
