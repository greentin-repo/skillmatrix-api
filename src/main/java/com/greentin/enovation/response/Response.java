package com.greentin.enovation.response;

import java.util.List;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SetupData;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;


public class Response {

	private String status;

	private Integer statusCode;

	private String reason;

	private boolean result;

	private List<SetupData> data;

	private List<SetupMaster> setupmasterList;

	private List<ProductOrgConfig>  productorglist;
	
	private List<ThresholdSetup> thSetup;
	
	private List<SocialMedia> socialMedia;
	
	private List<NoticeSetup> noticeSetup;

	public List<SetupData> getData() {
		return data;
	}

	public void setData(List<SetupData> data) {
		this.data = data;
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

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<SetupMaster> getSetupmasterList() {
		return setupmasterList;
	}

	public void setSetupmasterList(List<SetupMaster> setupmasterList) {
		this.setupmasterList = setupmasterList;
	}

	public List<ProductOrgConfig> getProductorglist() {
		return productorglist;
	}

	public void setProductorglist(List<ProductOrgConfig> productorglist) {
		this.productorglist = productorglist;
	}

	public List<ThresholdSetup> getThSetup() {
		return thSetup;
	}

	public void setThSetup(List<ThresholdSetup> thSetup) {
		this.thSetup = thSetup;
	}

	public List<SocialMedia> getSocialMedia() {
		return socialMedia;
	}

	public void setSocialMedia(List<SocialMedia> socialMedia) {
		this.socialMedia = socialMedia;
	}

	public List<NoticeSetup> getNoticeSetup() {
		return noticeSetup;
	}

	public void setNoticeSetup(List<NoticeSetup> noticeSetup) {
		this.noticeSetup = noticeSetup;
	}
	
	
	

}
