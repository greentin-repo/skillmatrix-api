package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@Table(name="master_rewards")
public class RewardsMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rwdId;
	private String rewardName;
	private String image;
	private double points;


	public RewardsMaster() {
		super();
	}
	public RewardsMaster(int rwdId) {
		super();
		this.rwdId = rwdId;
	}
	public int getRwdId() {
		return rwdId;
	}
	public void setRwdId(int rwdId) {
		this.rwdId = rwdId;
	}
	public String getRewardName() {
		return rewardName;
	}
	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	







}
