package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class RedeemCartRewards {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id")
	private EmployeeDetails empdetails;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cart_id")
	private Cart cart;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reward_resp_person_id")
	private RewardResponsibleData rewardresp;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status_id")
	private RedeemStatus status;
	
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	private int quantity;
	
	private int totalPoint;
	
	private int productPoint;
	
	@Transient 
	private int allPoints;
	
	@Transient
	private int redeemPoints;

	@Transient
	private String giftCode;

	@Transient
	private int empId;
	
	@Transient
	private int rewardRespId;
	
	@Transient
	private String description;
	
	@Transient
	private int points;
	
	@Transient
	private String name;
	
	@Transient
	private String cmpEmpId;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public EmployeeDetails getEmpdetails() {
		return empdetails;
	}

	public void setEmpdetails(EmployeeDetails empdetails) {
		this.empdetails = empdetails;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public int getRedeemPoints() {
		return redeemPoints;
	}

	public void setRedeemPoints(int redeemPoints) {
		this.redeemPoints = redeemPoints;
	}

	public String getGiftCode() {
		return giftCode;
	}

	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}

	public int getProductPoint() {
		return productPoint;
	}

	public void setProductPoint(int productPoint) {
		this.productPoint = productPoint;
	}

	public int getAllPoints() {
		return allPoints;
	}

	public void setAllPoints(int allPoints) {
		this.allPoints = allPoints;
	}

	public RewardResponsibleData getRewardresp() {
		return rewardresp;
	}

	public void setRewardresp(RewardResponsibleData rewardresp) {
		this.rewardresp = rewardresp;
	}

	public RedeemCartRewards() {
		super();
	}

	public RedeemCartRewards(int id) {
		super();
		this.id = id;
	}

	public int getRewardRespId() {
		return rewardRespId;
	}

	public void setRewardRespId(int rewardRespId) {
		this.rewardRespId = rewardRespId;
	}

	public RedeemStatus getStatus() {
		return status;
	}

	public void setStatus(RedeemStatus status) {
		this.status = status;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCmpEmpId() {
		return cmpEmpId;
	}

	public void setCmpEmpId(String cmpEmpId) {
		this.cmpEmpId = cmpEmpId;
	}
	
	

}
