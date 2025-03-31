package com.greentin.enovation.dto;

public class EmpDetailsDtoForEmail {

	private int empId;

	private String firstName;

	private String lastName;

	private String emailId;

	private String contact;

	public EmpDetailsDtoForEmail() {
		super();
	}

	public EmpDetailsDtoForEmail(int empId, String firstName, String lastName, String emailId, String contact) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.contact = contact;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}


}
