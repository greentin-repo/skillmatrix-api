package com.greentin.enovation.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="master_point_level")
//@javax.persistence.Cacheable
public class PointLevelMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int levelId;

	private String title;

	private double points;
	
	@ManyToOne()
	@JoinColumn(name="orgId")
	private OrganizationMaster org;

    public PointLevelMaster() {
		super();
	}
	public PointLevelMaster(int levelId) {
		super();
		this.levelId = levelId;
	}
	public int getLevelId() {
		return levelId;
	}
	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPoints() {
		return points;
	}
	public void setPoints(double points) {
		this.points = points;
	}
	public OrganizationMaster getOrg() {
		return org;
	}
	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}


}
