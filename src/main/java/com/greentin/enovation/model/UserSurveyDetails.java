package com.greentin.enovation.model;


import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="user_survey_details")
public class UserSurveyDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userSurveyId;

	@ManyToOne
	@JoinColumn(name= "empId")
	private EmployeeDetails employeeDetails;

	@ManyToOne
	@JoinColumn(name= "surveyId")
	private SurveyDetails surveyDetails;
	
	@ManyToOne
	@JoinColumn(name= "statusId")
	private SurveyStatusMaster status;

	private Timestamp startDate;
	
	private Timestamp completedDate;

	@OneToMany(mappedBy = "empSurveyAnswer",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<UserAnswers> empSureyAnsOption;

	
	

	public List<UserAnswers> getEmpSureyAnsOption() {
		return empSureyAnsOption;
	}

	public void setEmpSureyAnsOption(List<UserAnswers> empSureyAnsOption) {
		this.empSureyAnsOption = empSureyAnsOption;
	}

	public UserSurveyDetails() {
		super();
	}

	public UserSurveyDetails(int empSurveyAnswerId) {
		super();
		this.userSurveyId = empSurveyAnswerId;
	}

	public int getUserSurveyId() {
		return userSurveyId;
	}

	public void setUserSurveyId(int empSurveyAnswerId) {
		this.userSurveyId = empSurveyAnswerId;
	}

	public EmployeeDetails getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeDetails employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public SurveyDetails getSurveyDetails() {
		return surveyDetails;
	}

	public void setSurveyDetails(SurveyDetails surveyDetails) {
		this.surveyDetails = surveyDetails;
	}

	/*public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}*/
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Timestamp getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Timestamp completedDate) {
		this.completedDate = completedDate;
	}
	
	public SurveyStatusMaster getStatus() {
		return status;
	}

	public void setStatus(SurveyStatusMaster status) {
		this.status = status;
	}
	
}
