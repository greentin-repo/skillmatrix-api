
package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="emp_id")
	private EmployeeDetails empdetails;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reward_id")
	private Rewards rewards;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vendor_id")
	private Vendor vendor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reward_resp_id")
	private RewardResponsibleData rewardRespData;
	
	private int isOrder;
	
	private String cartStatus;
	
	@CreationTimestamp
	private Timestamp createdDate;
	
	public int getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private int quantity;

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

	public Rewards getRewards() {
		return rewards;
	}

	public void setRewards(Rewards rewards) {
		this.rewards = rewards;
	}
	public Cart() {
		super();
	}

	public Cart(int id) {
		super();
		this.id = id;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public OrganizationMaster getOrg() {
		return org;
	}

	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}

	public String getCartStatus() {
		return cartStatus;
	}

	public void setCartStatus(String cartStatus) {
		this.cartStatus = cartStatus;
	}

	public Cart(String cartStatus) {
		super();
		this.cartStatus = cartStatus;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public RewardResponsibleData getRewardRespData() {
		return rewardRespData;
	}

	public void setRewardRespData(RewardResponsibleData rewardRespData) {
		this.rewardRespData = rewardRespData;
	}
	
	
}

