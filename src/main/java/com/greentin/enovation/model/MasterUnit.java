package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "master_unit")
public class MasterUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Column(name = "unit", columnDefinition = "varchar(255) default 'NA'")
	private String unit;

	@Column(name = "caption", columnDefinition = "varchar(255) default 'NA'")
	private String caption;

	@Column(name = "label", columnDefinition = "varchar(255) default 'NA'")
	private String label;

	/*
	 * @Column(name = "branch_id", columnDefinition = "int default 0") private int
	 * branchId;
	 * 
	 * @Column(name = "created_by", columnDefinition = "int default 0", updatable =
	 * false) private int createdBy;
	 * 
	 * @Column(name = "updated_by", columnDefinition = "int default 0") private int
	 * updatedBy;
	 * 
	 * @CreationTimestamp
	 * 
	 * @Column(name = "created_date", updatable = false, columnDefinition =
	 * "datetime default now()") private Timestamp createdDate;
	 * 
	 * @UpdateTimestamp
	 * 
	 * @Column(name = "updated_date", columnDefinition = "datetime default now()")
	 * private Timestamp updatedDate;
	 */

	public MasterUnit() {
		super();
	}

	public MasterUnit(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	/*
	 * public int getBranchId() { return branchId; }
	 * 
	 * public void setBranchId(int branchId) { this.branchId = branchId; }
	 * 
	 * public int getCreatedBy() { return createdBy; }
	 * 
	 * public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
	 * 
	 * public int getUpdatedBy() { return updatedBy; }
	 * 
	 * public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }
	 * 
	 * public Timestamp getCreatedDate() { return createdDate; }
	 * 
	 * public void setCreatedDate(Timestamp createdDate) { this.createdDate =
	 * createdDate; }
	 * 
	 * public Timestamp getUpdatedDate() { return updatedDate; }
	 * 
	 * public void setUpdatedDate(Timestamp updatedDate) { this.updatedDate =
	 * updatedDate; }
	 */

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
