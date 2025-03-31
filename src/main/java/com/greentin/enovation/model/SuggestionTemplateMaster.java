package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;


@Entity
@Table(name="master_suggestion_template")
public class SuggestionTemplateMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String  templateName;

	private String  templatePreviewUrl;

	@Lob
	private String jasperTemplateUrl;

	@Lob
	private String htmlCode;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	private int isEnable;

	@Transient
	private MultipartFile templateImg;

	public SuggestionTemplateMaster() {
		super();
	}

	public SuggestionTemplateMaster(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTemplatePreviewUrl() {
		if(templatePreviewUrl==null) {
			return templatePreviewUrl;	
		}else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+templatePreviewUrl;
		}
	}

	public void setTemplatePreviewUrl(String templatePreviewUrl) {
		this.templatePreviewUrl = templatePreviewUrl;
	}

	public String getJasperTemplateUrl() {
		return jasperTemplateUrl;
	}

	public void setJasperTemplateUrl(String jasperTemplateUrl) {
		this.jasperTemplateUrl = jasperTemplateUrl;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}

	public String getHtmlCode() {
		return htmlCode;
	}

	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public MultipartFile getTemplateImg() {
		return templateImg;
	}

	public void setTemplateImg(MultipartFile templateImg) {
		this.templateImg = templateImg;
	}

}
