package com.greentin.enovation.accesscontrol;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.OrganizationMaster;

//@Entity
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

	/*@NotBlank
	@Size(max = 15)
	@Column(nullable=true)
	private String username;*/

	@NotBlank
	@Size(max = 40)
	@Email
	private String email;

	private String orgDomain;

	private String alies;

	@Size(max = 100)
	private String password;

	private int isEmailVerified;

	private int isActive;

	private Date regDate;

	private String contactNumber;

	private String designation;

	private String country;

	private String state;

	private Date createdDate;

	/*
	@OneToOne()
	@JoinColumn(name="empId",unique=true)
	private EmployeeDetails empDetails;
	 */
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column()
	@Lob
	private String token;

	@Transient
	private String location;

	@Transient
	private String orgName;

	@Transient
	private Integer productId;

	@Transient
	private Integer subscripId;

	@Transient
	private String roleName;


	/*	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
	joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	 */
	@ManyToOne()
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne()
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	public Users(Long id) {
		super();
		this.id = id;
	}

	public Users() {
		super();
	}

	public Users(String name2, String username, String email2, String password2) {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}*/

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(int isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	/*	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	 */	public OrganizationMaster getOrg() {
		 return org;
	 }

	 public void setOrg(OrganizationMaster org) {
		 this.org = org;
	 }

	 public String getContactNumber() {
		 return contactNumber;
	 }

	 public void setContactNumber(String contactNumber) {
		 this.contactNumber = contactNumber;
	 }

	 public String getDesignation() {
		 return designation;
	 }

	 public void setDesignation(String designation) {
		 this.designation = designation;
	 }

	 public String getLocation() {
		 return location;
	 }

	 public void setLocation(String location) {
		 this.location = location;
	 }

	 public String getOrgName() {
		 return orgName;
	 }

	 public void setOrgName(String orgName) {
		 this.orgName = orgName;
	 }

	 public Integer getProductId() {
		 return productId;
	 }

	 public void setProductId(Integer productId) {
		 this.productId = productId;
	 }

	 public Integer getSubscripId() {
		 return subscripId;
	 }

	 public void setSubscripId(Integer subscripId) {
		 this.subscripId = subscripId;
	 }

	 public String getToken() {
		 return token;
	 }

	 public void setToken(String token) {
		 this.token = token;
	 }

	 public String getRoleName() {
		 return roleName;
	 }

	 public void setRoleName(String roleName) {
		 this.roleName = roleName;
	 }

	 public BranchMaster getBranch() {
		 return branch;
	 }

	 public void setBranch(BranchMaster branch) {
		 this.branch = branch;
	 }

	 /*	public EmployeeDetails getEmpDetails() {
		return empDetails;
	}

	public void setEmpDetails(EmployeeDetails empDetails) {
		this.empDetails = empDetails;
	}
	  */
	 public Date getCreatedDate() {
		 return createdDate;
	 }

	 public void setCreatedDate(Date createdDate) {
		 this.createdDate = createdDate;
	 }

	 public String getOrgDomain() {
		 return orgDomain;
	 }

	 public void setOrgDomain(String orgDomain) {
		 this.orgDomain = orgDomain;
	 }

	 public String getAlies() {
		 return alies;
	 }

	 public void setAlies(String alies) {
		 this.alies = alies;
	 }

}
