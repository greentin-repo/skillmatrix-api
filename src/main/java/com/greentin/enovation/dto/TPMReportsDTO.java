package com.greentin.enovation.dto;

import java.sql.Timestamp;

public class TPMReportsDTO {
	
	//tt.type, td.tag_number,e.first_name,e.last_name, 
	//td.created_date,dept.dept_name, ea.first_name,ea.last_name,tsa.created_date,ms.label
	
	private String tagType;
	
	private String tagNumber;
	
	private String empName;
	
	private Timestamp createdDate;
	
	private String raisedByDeptName;
	
	private String assignedToName;
	
	private Timestamp assignedDate;
	
	private String label;
	
	private Timestamp closureDate;
	
	private long tpmId;
	
	private int totalCount;
	
	private int totalWhiteTagCount;
	
	private int whiteTagOpenCount;
	
	private int totalRedTagCount;
	
	private int redTagOpenCount;
	
	private int totalBreakdownTagCount;
	
	private int breakdownTagOpenCount;
	
	private String lineName;
	
	private String targetDate;
	
	private long days;

	private String machName;
	
	private String machNumber;
	
	private String partName;
	
	private String partNumber;
	
	private String abnormality;
	
	private int isRepeat;
	
	private long id;
	
	private String content;
	
	public TPMReportsDTO() {
		super();
	}

	public TPMReportsDTO(int totalCount, int totalWhiteTagCount, int whiteTagOpenCount, int totalRedTagCount,
			int redTagOpenCount, int totalBreakdownTagCount, int breakdownTagOpenCount) {
		super();
		this.totalCount = totalCount;
		this.totalWhiteTagCount = totalWhiteTagCount;
		this.whiteTagOpenCount = whiteTagOpenCount;
		this.totalRedTagCount = totalRedTagCount;
		this.redTagOpenCount = redTagOpenCount;
		this.totalBreakdownTagCount = totalBreakdownTagCount;
		this.breakdownTagOpenCount = breakdownTagOpenCount;
	}
	


	public TPMReportsDTO(String tagType, String tagNumber, String empName, Timestamp createdDate,
			String raisedByDeptName, String assignedToName, Timestamp assignedDate, String label,Timestamp closureDate, long tpmId,
			String machNumber, String machName, String partNumber, String partName,String abnormality, int isRepeat,String lineName,String targetDate,long days) {
		super();
		this.tagType = tagType;
		this.tagNumber = tagNumber;
		this.empName = empName;
		this.createdDate = createdDate;
		this.raisedByDeptName = raisedByDeptName;
		this.assignedToName = assignedToName;
		this.assignedDate = assignedDate;
		this.label = label;
		this.closureDate=closureDate;
		this.tpmId=tpmId;
		this.machNumber = machNumber;
		this.machName = machName;
		this.partNumber=partNumber;
		this.partName=partName;
		this.abnormality=abnormality;
		this.isRepeat=isRepeat;
		this.lineName=lineName;
		this.targetDate=targetDate;
		this.days=days;
	}

	/**
	 * @return the tagType
	 */
	public String getTagType() {
		return tagType;
	}

	/**
	 * @param tagType the tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	/**
	 * @return the tagNumber
	 */
	public String getTagNumber() {
		return tagNumber;
	}

	/**
	 * @param tagNumber the tagNumber to set
	 */
	public void setTagNumber(String tagNumber) {
		this.tagNumber = tagNumber;
	}

	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}

	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}

	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the raisedByDeptName
	 */
	public String getRaisedByDeptName() {
		return raisedByDeptName;
	}

	/**
	 * @param raisedByDeptName the raisedByDeptName to set
	 */
	public void setRaisedByDeptName(String raisedByDeptName) {
		this.raisedByDeptName = raisedByDeptName;
	}

	/**
	 * @return the assignedToName
	 */
	public String getAssignedToName() {
		return assignedToName;
	}

	/**
	 * @param assignedToName the assignedToName to set
	 */
	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}

	/**
	 * @return the assignedDate
	 */
	public Timestamp getAssignedDate() {
		return assignedDate;
	}

	/**
	 * @param assignedDate the assignedDate to set
	 */
	public void setAssignedDate(Timestamp assignedDate) {
		this.assignedDate = assignedDate;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalWhiteTagCount() {
		return totalWhiteTagCount;
	}

	public void setTotalWhiteTagCount(int totalWhiteTagCount) {
		this.totalWhiteTagCount = totalWhiteTagCount;
	}

	public int getWhiteTagOpenCount() {
		return whiteTagOpenCount;
	}

	public void setWhiteTagOpenCount(int whiteTagOpenCount) {
		this.whiteTagOpenCount = whiteTagOpenCount;
	}

	public int getTotalRedTagCount() {
		return totalRedTagCount;
	}

	public void setTotalRedTagCount(int totalRedTagCount) {
		this.totalRedTagCount = totalRedTagCount;
	}

	public int getRedTagOpenCount() {
		return redTagOpenCount;
	}

	public void setRedTagOpenCount(int redTagOpenCount) {
		this.redTagOpenCount = redTagOpenCount;
	}

	public int getTotalBreakdownTagCount() {
		return totalBreakdownTagCount;
	}

	public void setTotalBreakdownTagCount(int totalBreakdownTagCount) {
		this.totalBreakdownTagCount = totalBreakdownTagCount;
	}

	public int getBreakdownTagOpenCount() {
		return breakdownTagOpenCount;
	}

	public void setBreakdownTagOpenCount(int breakdownTagOpenCount) {
		this.breakdownTagOpenCount = breakdownTagOpenCount;
	}

	public Timestamp getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Timestamp closureDate) {
		this.closureDate = closureDate;
	}

	public long getTpmId() {
		return tpmId;
	}

	public void setTpmId(long tpmId) {
		this.tpmId = tpmId;
	}

	public String getMachName() {
		return machName;
	}

	public void setMachName(String machName) {
		this.machName = machName;
	}

	public String getMachNumber() {
		return machNumber;
	}

	public void setMachNumber(String machNumber) {
		this.machNumber = machNumber;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(String targetDate) {
		this.targetDate = targetDate;
	}

	public long getDays() {
		return days;
	}

	public void setDays(long days) {
		this.days = days;
	}
	public String getAbnormality() {
		return abnormality;
	}

	public void setAbnormality(String abnormality) {
		this.abnormality = abnormality;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
    
}