package com.greentin.enovation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "master_status")
//@javax.persistence.Cacheable
public class StatusMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "status_id", unique = true, nullable = false)
	private int statusId;

	private String status;

	private String label;

	private int typeId;

	private int pointsInflight;

	private int typeEvalId;

	private int executiveInflight;

	private int evaluatorInflight;

	private int implementorInflight;

	private int controllerInflight;

	private int roleId;

	private int pointsRoleId;

	private String applicationName; // SUGGESTION, KUBER, AUDIT

	/**
	 * 
	 * This will be only Applicable for Suggestion to identify either it is pre
	 * implementation or post implementation status
	 */
	private String processType;// PRE or POST

	@Column(name = "pms_role_id", columnDefinition = "int default 0")
	private long pmsRoleId;

	@ManyToOne()
	@JoinColumn(name = "filter_id")
	private filterType filterType;

	public StatusMaster() {
		super();
	}

	public StatusMaster(int statusId) {
		super();
		this.statusId = statusId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getTypeEvalId() {
		return typeEvalId;
	}

	public void setTypeEvalId(int typeEvalId) {
		this.typeEvalId = typeEvalId;
	}

	public filterType getFilterType() {
		return filterType;
	}

	public void setFilterType(filterType filterType) {
		this.filterType = filterType;
	}

	public int getExecutiveInflight() {
		return executiveInflight;
	}

	public void setExecutiveInflight(int executiveInflight) {
		this.executiveInflight = executiveInflight;
	}

	public int getEvaluatorInflight() {
		return evaluatorInflight;
	}

	public void setEvaluatorInflight(int evaluatorInflight) {
		this.evaluatorInflight = evaluatorInflight;
	}

	public int getImplementorInflight() {
		return implementorInflight;
	}

	public void setImplementorInflight(int implementorInflight) {
		this.implementorInflight = implementorInflight;
	}

	public int getControllerInflight() {
		return controllerInflight;
	}

	public void setControllerInflight(int controllerInflight) {
		this.controllerInflight = controllerInflight;
	}

	public int getPointsInflight() {
		return pointsInflight;
	}

	public void setPointsInflight(int pointsInflight) {
		this.pointsInflight = pointsInflight;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getPointsRoleId() {
		return pointsRoleId;
	}

	public void setPointsRoleId(int pointsRoleId) {
		this.pointsRoleId = pointsRoleId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public long getPmsRoleId() {
		return pmsRoleId;
	}

	public void setPmsRoleId(long pmsRoleId) {
		this.pmsRoleId = pmsRoleId;
	}

}
