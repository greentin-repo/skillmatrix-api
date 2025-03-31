package com.greentin.enovation.accesscontrol;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class SubMenu {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String subMenuName;

	private Integer status ;

	private Date createDate;

	private Date updateDate;

	@ManyToOne()
	@JoinColumn(name="menu_id")
	@JsonBackReference
	private Menu menu;

	@Transient
	private int menuId;

	@Transient
	private Long createdById;

	@Transient
	private Long updatedById;



	public SubMenu(int id) {
		super();
		this.id = id;
	}

	public SubMenu() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Long getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Long updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}





}
