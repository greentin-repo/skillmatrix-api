package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;

@Entity
public class MachineParts {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	/*for add
	 * /addMachineParts
	 * [{partNumber
	 * partName
	 * machineDetails:{
	 *       machId:1
	 *       }
	 *       }]
	 * */

	/*for UPDATE
	 * /addMachineParts
	 * [{
	 * id
	 * partNumber
	 * partName
	 * machineDetails:{
	 *       machId:1
	 *       }
	 *createdDate:
	 *updatedDate:
	 *}]
	 * */

	private String partNumber;

	private String partName;
	
	private String imgUrl;
	
	
	@Transient
	private MultipartFile machImg;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;
	
	@Transient
	private String lineName;
	
	@Transient
	private long lineId;
	
	@Transient
	private String deptName;

	@Transient
	private int deptId;

	@ManyToOne
	@JoinColumn(name="machine_id")
	MachineDetails machineDetails;
	
	public MachineParts(long id, String partNumber, String partName, Timestamp createdDate, String imgUrl,String lineName,long lineId,String deptName,int deptId) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partName = partName;
		this.createdDate=createdDate;
		this.imgUrl=imgUrl;
		this.lineName=lineName;
		this.lineId=lineId;
		this.deptName=deptName;
		this.deptId=deptId;
	}
	
	public MachineParts(long id, String partNumber, String partName, Timestamp createdDate, String imgUrl,String lineName,long lineId,String deptName,int deptId,MachineDetails machineDetails) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partName = partName;
		this.createdDate=createdDate;
		this.imgUrl=imgUrl;
		this.lineName=lineName;
		this.lineId=lineId;
		this.deptName=deptName;
		this.deptId=deptId;
		this.machineDetails=machineDetails;
	}

	public MachineParts() {
		super();
	}

	public MachineParts(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
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

	public MachineDetails getMachineDetails() {
		return machineDetails;
	}

	public void setMachineDetails(MachineDetails machineDetails) {
		this.machineDetails = machineDetails;
	}

	public String getImgUrl() {
		if(imgUrl==null) {
			return imgUrl;	
		}else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+imgUrl;
		}
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public MultipartFile getMachImg() {
		return machImg;
	}

	public void setMachImg(MultipartFile machImg) {
		this.machImg = machImg;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public long getLineId() {
		return lineId;
	}

	public void setLineId(long lineId) {
		this.lineId = lineId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	
	
}
