package com.greentin.enovation.accesscontrol;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Menu {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String menuName;

	private Integer status;

	private Date createDate;

	private Date updateDate;

	

	@ManyToOne()
	@JoinColumn(name="product_id")
	private Product product;

	@OneToMany(mappedBy="menu")
	@JsonManagedReference
	List<SubMenu> submenuList;

	@Transient
	private Long createdById;

	@Transient
	private Long updatedById;

	@Transient
	private int productId;





	public Menu(int id) {
		super();
		this.id = id;
	}

	public Menu() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}



	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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


	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public Long getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Long updatedById) {
		this.updatedById = updatedById;
	}

	public List<SubMenu> getSubmenuList() {
		return submenuList;
	}

	public void setSubmenuList(List<SubMenu> submenuList) {
		this.submenuList = submenuList;
	}


}
