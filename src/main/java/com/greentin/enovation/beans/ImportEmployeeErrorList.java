package com.greentin.enovation.beans;

public class ImportEmployeeErrorList {

	private String employeeId;

	private String firstName;

	private String lastName;

	private String contactNumber;

	private String emailId;

	private String department;

	private String designation;

	private String comment;





	public ImportEmployeeErrorList() {
		super();
	}

	public ImportEmployeeErrorList(String employeeId, String firstName, String lastName,
			String emailId,String contactNumber, String department, String comment, String designation) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.emailId = emailId;
		this.department = department;
		this.comment = comment;
		this.designation=designation;
	}

	public ImportEmployeeErrorList(String employeeId, String firstName, String lastName, 
			String emailId,String contactNumber, String department, String designation) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contactNumber = contactNumber;
		this.emailId = emailId;
		this.department = department;
		this.designation=designation;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}


}
