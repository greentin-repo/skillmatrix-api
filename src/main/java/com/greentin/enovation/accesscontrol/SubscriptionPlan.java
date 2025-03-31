package com.greentin.enovation.accesscontrol;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class SubscriptionPlan {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer subscripId;

	private String type;

	private String description;

	private Integer noOfEmployee;
	
	private Integer noOfDays;

	private int isDeactive;

	private Date createdDate;

	@ManyToOne()
	@JoinColumn(name="product_id")
	private Product product;





	public SubscriptionPlan() {
		super();
	}

	public SubscriptionPlan(Integer subscripId) {
		super();
		this.subscripId = subscripId;
	}

	public Integer getSubscripId() {
		return subscripId;
	}

	public void setSubscripId(Integer subscripId) {
		this.subscripId = subscripId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoOfEmployee() {
		return noOfEmployee;
	}

	public void setNoOfEmployee(Integer noOfEmployee) {
		this.noOfEmployee = noOfEmployee;
	}

	public int getIsDeactive() {
		return isDeactive;
	}

	public void setIsDeactive(int isDeactive) {
		this.isDeactive = isDeactive;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Integer noOfDays) {
		this.noOfDays = noOfDays;
	}


}
