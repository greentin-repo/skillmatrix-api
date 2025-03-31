package com.greentin.enovation.dto;

public class AuditReportDto {

	private String sectionName;

	private String question;

	private String answer;

	private String score;

	private String nonComNumber;

	private String name;
	private String status;
	private String issueDesc;
	private String comment;

	private AuditReportDto auditee;


	public AuditReportDto(String name) {
		super();
		this.name = name;
	}
	private String auditeeDoc;
	private String asigneeDoc;
	private String assigneeDate;
	private String asigneeName;
	private String asigneeStatus;
	private String asigneeRootCause;
	private String asigneeCorrectiveAction;
	public AuditReportDto(String nonComNumber, String question,String name, String status, String issueDesc, String comment,
			String asigneeName,String asigneeStatus, String assigneeDate,String asigneeRootCause, String asigneeCorrectiveAction,
			String auditeeDoc, String asigneeDoc ) {
		super();
		this.nonComNumber = nonComNumber;
		this.question=question;
		this.name=name;
		this.status=status;
		this.issueDesc=issueDesc;
		this.comment=comment;
		this.asigneeName=asigneeName;
		this.asigneeStatus=asigneeStatus;
		this.assigneeDate=assigneeDate;
		this.asigneeRootCause=asigneeRootCause;
		this.asigneeCorrectiveAction=asigneeCorrectiveAction;
		this.auditeeDoc=auditeeDoc;
		this.asigneeDoc=asigneeDoc;
	}

	public AuditReportDto(String sectionName,String question, String answer, String score ) {
		super();
		this.sectionName=sectionName;
		this.question = question;
		this.answer = answer;
		this.score = score;
	}


	public AuditReportDto() {
		super();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getNonComNumber() {
		return nonComNumber;
	}

	public void setNonComNumber(String nonComNumber) {
		this.nonComNumber = nonComNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getIssueDesc() {
		return issueDesc;
	}

	public void setIssueDesc(String issueDesc) {
		this.issueDesc = issueDesc;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public AuditReportDto getAuditee() {
		return auditee;
	}

	public void setAuditee(AuditReportDto auditee) {
		this.auditee = auditee;
	}

	public String getAuditeeDoc() {
		return auditeeDoc;
	}

	public void setAuditeeDoc(String auditeeDoc) {
		this.auditeeDoc = auditeeDoc;
	}

	public String getAsigneeDoc() {
		return asigneeDoc;
	}

	public void setAsigneeDoc(String asigneeDoc) {
		this.asigneeDoc = asigneeDoc;
	}

	public String getAssigneeDate() {
		return assigneeDate;
	}

	public void setAssigneeDate(String assigneeDate) {
		this.assigneeDate = assigneeDate;
	}

	public String getAsigneeName() {
		return asigneeName;
	}

	public void setAsigneeName(String asigneeName) {
		this.asigneeName = asigneeName;
	}

	public String getAsigneeStatus() {
		return asigneeStatus;
	}

	public void setAsigneeStatus(String asigneeStatus) {
		this.asigneeStatus = asigneeStatus;
	}

	public String getAsigneeRootCause() {
		return asigneeRootCause;
	}

	public void setAsigneeRootCause(String asigneeRootCause) {
		this.asigneeRootCause = asigneeRootCause;
	}

	public String getAsigneeCorrectiveAction() {
		return asigneeCorrectiveAction;
	}

	public void setAsigneeCorrectiveAction(String asigneeCorrectiveAction) {
		this.asigneeCorrectiveAction = asigneeCorrectiveAction;
	}




}
