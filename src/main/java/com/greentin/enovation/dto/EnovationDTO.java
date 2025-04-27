/**
 * @author Vinay B. May 18, 2022 6:44:08 PM
 * DTO
 */
package com.greentin.enovation.dto;

import java.util.HashMap;
import java.util.List;

/**
 * @author Vinay B. May 18, 2022 6:44:08 PM
 * @type_name DTO
 */
public class EnovationDTO {

	private int id;

	private int empId;

	private int transferTo;

	private int stageId;

	private List<HashMap<String, List<EnovationDTO>>> data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public List<HashMap<String, List<EnovationDTO>>> getData() {
		return data;
	}

	public void setData(List<HashMap<String, List<EnovationDTO>>> data) {
		this.data = data;
	}

	public int getTransferTo() {
		return transferTo;
	}

	public void setTransferTo(int transferTo) {
		this.transferTo = transferTo;
	}

	public int getStageId() {
		return stageId;
	}

	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
}
