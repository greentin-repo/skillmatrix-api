package com.greentin.enovation.dto;

public class MailDTO {

	private String toMail;
	private String subject;
	private String content;
	private String attachmentPath;

	public MailDTO() {
		super();
	}
	
	public MailDTO(String toMail, String subject, String content, String attachmentPath) {
		super();
		this.toMail = toMail;
		this.subject = subject;
		this.content = content;
		this.attachmentPath = attachmentPath;
	}

	public MailDTO(String toMail, String subject, String content) {
		super();
		this.toMail = toMail;
		this.subject = subject;
		this.content = content;
	}
	public String getToMail() {
		return toMail;
	}
	public void setToMail(String toMail) {
		this.toMail = toMail;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}


}
