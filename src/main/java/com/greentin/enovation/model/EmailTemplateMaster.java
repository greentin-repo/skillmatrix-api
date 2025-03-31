package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="email_template_master")
//@javax.persistence.Cacheable
public class EmailTemplateMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="email_tmplt_id" ,unique=true, nullable=false)
	private int emailTmpltId;
	
	private String body,name;
	@Column(name="description")
	private String desc;
	
	private String subject;
	
	private String fcmBody;


	public int getEmailTmpltId() {
		return emailTmpltId;
	}
	public void setEmailTmpltId(int emailTmpltId) {
		this.emailTmpltId = emailTmpltId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFcmBody() {
		return fcmBody;
	}
	public void setFcmBody(String fcmBody) {
		this.fcmBody = fcmBody;
	}

}
