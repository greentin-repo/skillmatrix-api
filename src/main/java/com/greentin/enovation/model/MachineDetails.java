package com.greentin.enovation.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.model.dwm.Line;

@Entity
@Table(name="master_machine_details")
public class MachineDetails {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int machId;

	private String machNumber;

	private String machName;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dept_id")
	private DepartmentMaster dept;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="line_id")
	private Line line;

	private int isEnable;

	@Transient
	private String machNameAndNumber;
	
	@Transient
	private MultipartFile machineImg;
	
	@Transient
	private String lineName;
	
	@Transient
	private long lineId;


	private String imgUrl;
	
	@Transient
	private int deptId;
	

	public MachineDetails() {
		super();
	}

	public MachineDetails(int machId, String machNumber, String machName, BranchMaster branch, DepartmentMaster dept,
			String machNameAndNumber, int isEnable, List<MachineParts> machineParts) {
		super();
		this.machId = machId;
		this.machNumber = machNumber;
		this.machName = machName;
		this.dept = dept;
		this.branch = branch;
		this.machNameAndNumber=machNameAndNumber;
		this.isEnable = isEnable;
		this.machineParts = machineParts;
		
	}
	
	public MachineDetails(int machId, String machNumber, String machName, BranchMaster branch, DepartmentMaster dept,
			String machNameAndNumber, int isEnable, String imgUrl, String lineName, long lineId) {
		super();
		this.machId = machId;
		this.machNumber = machNumber;
		this.machName = machName;
		this.dept = dept;
		this.branch = branch;
		this.machNameAndNumber=machNameAndNumber;
		this.isEnable = isEnable;
		this.imgUrl = imgUrl;
		this.lineName=lineName;
		this.lineId=lineId;
	}

	//@OneToMany(mappedBy="machineDetails")
	//@JsonManagedReference
	@Transient
	List<MachineParts> machineParts;
	
	
	public MachineDetails(int machId) {
		super();
		this.machId = machId;
	}

	

	public int getMachId() {
		return machId;
	}

	public void setMachId(int machId) {
		this.machId = machId;
	}

	public String getMachNumber() {
		return machNumber;
	}

	public void setMachNumber(String machNumber) {
		this.machNumber = machNumber;
	}

	public String getMachName() {
		return machName;
	}

	public void setMachName(String machName) {
		this.machName = machName;
	}

	public DepartmentMaster getDept() {
		return dept;
	}

	public void setDept(DepartmentMaster dept) {
		this.dept = dept;
	}

	public BranchMaster getBranch() {
		return branch;
	}

	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}

	public String getMachNameAndNumber() {
		return machNameAndNumber;
	}

	public void setMachNameAndNumber(String machNameAndNumber) {
		this.machNameAndNumber = machNameAndNumber;
	}

	public int getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public List<MachineParts> getMachineParts() {
		return machineParts;
	}

	public void setMachineParts(List<MachineParts> machineParts) {
		this.machineParts = machineParts;
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
	
	public MultipartFile getMachineImg() {
		return machineImg;
	}

	public void setMachineImg(MultipartFile machineImg) {
		this.machineImg = machineImg;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
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

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

}
