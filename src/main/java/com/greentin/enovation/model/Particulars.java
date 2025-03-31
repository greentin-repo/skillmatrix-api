package com.greentin.enovation.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
// @javax.persistence.Cacheable
public class Particulars {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String particulars;

	private int points;

	private int createdById;

	private int updatedById;

	@ManyToOne
	@JoinColumn(name = "cat_id")
	@JsonBackReference
	private CategoryMaster category;
	
	@OneToMany(mappedBy = "particulars", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ParticularsPoint> particularPoints;
	
	@Column(name = "is_tangible", columnDefinition = "tinyint(1) default 0")
	private boolean isTangible;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;

	public Particulars() {
		super();
	}

	public Particulars(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParticulars() {
		return particulars;
	}

	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getCreatedById() {
		return createdById;
	}

	public void setCreatedById(int createdById) {
		this.createdById = createdById;
	}

	public int getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(int updatedById) {
		this.updatedById = updatedById;
	}

	public CategoryMaster getCategory() {
		return category;
	}

	public void setCategory(CategoryMaster category) {
		this.category = category;
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

	public boolean isTangible() {
		return isTangible;
	}

	public void setTangible(boolean isTangible) {
		this.isTangible = isTangible;
	}

	public List<ParticularsPoint> getParticularPoints() {
		return particularPoints;
	}

	public void setParticularPoints(List<ParticularsPoint> particularPoints) {
		this.particularPoints = particularPoints;
	}
}
