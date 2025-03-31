package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tbl_emp_reward_details")
public class EmprewardDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int empRwdId;
	private double totalPoints;
	private double consumedPoints;
	private Date awardedDt;
	@ManyToOne()
	@JoinColumn(name = "rwd_id")
	private RewardsMaster rewards;
	@ManyToOne()
	@JoinColumn(name = "emp_id")
	private EmployeeDetails emp_det;
    private Timestamp createdDate;
	private Timestamp updatedDate;

	public int getEmpRwdId() {
		return empRwdId;
	}
	public void setEmpRwdId(int empRwdId) {
		this.empRwdId = empRwdId;
	}
	public double getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(double totalPoints) {
		this.totalPoints = totalPoints;
	}
	public double getConsumedPoints() {
		return consumedPoints;
	}
	public void setConsumedPoints(double consumedPoints) {
		this.consumedPoints = consumedPoints;
	}
	public Date getAwardedDt() {
		return awardedDt;
	}
	public void setAwardedDt(Date awardedDt) {
		this.awardedDt = awardedDt;
	}

	public RewardsMaster getRewards() {
		return rewards;
	}
	public void setRewards(RewardsMaster rewards) {
		this.rewards = rewards;
	}

	public EmployeeDetails getEmp_det() {
		return emp_det;
	}
	public void setEmp_det(EmployeeDetails emp_det) {
		this.emp_det = emp_det;
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


}
