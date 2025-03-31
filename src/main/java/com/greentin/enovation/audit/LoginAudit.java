package com.greentin.enovation.audit;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class LoginAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int loginTrackerId;
	
	private int empId;

	@CreationTimestamp
	private Timestamp loginDateTime;
	
	private Timestamp logoutDateTime;

	private String userIp;
	
	private String platform;
	
	private String userName;
	
	private String comment;
	
	private int branchId;
	
	private int orgId;
	
	@Lob
	private String responseString;
	
	
	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getLoginTrackerId() {
		return loginTrackerId;
	}

	public void setLoginTrackerId(int loginTrackerId) {
		this.loginTrackerId = loginTrackerId;
	}

	public Timestamp getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Timestamp loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public Timestamp getLogoutDateTime() {
		return logoutDateTime;
	}

	public void setLogoutDateTime(Timestamp logoutDateTime) {
		this.logoutDateTime = logoutDateTime;
	}


	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	
	
}
