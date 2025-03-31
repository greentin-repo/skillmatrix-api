package com.greentin.enovation.accesscontrol;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.greentin.enovation.model.RegistrationSource;

@Entity
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 40)
	private String name;

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

	@Column()
	@Lob
	private String token;

	private String location;

	private String orgName;

	private int productId;

	private int subscripId;
	
	@ManyToOne
	@JoinColumn(name="reg_source_id")
	private RegistrationSource regSource;

	@Transient
	private String portalLink;
	
	@Transient
	private int registrationSource;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getSubscripId() {
		return subscripId;
	}

	public void setSubscripId(int subscripId) {
		this.subscripId = subscripId;
	}

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public RegistrationSource getRegSource() {
		return regSource;
	}

	public void setRegSource(RegistrationSource regSource) {
		this.regSource = regSource;
	}

	public int getRegistrationSource() {
		return registrationSource;
	}

	public void setRegistrationSource(int registrationSource) {
		this.registrationSource = registrationSource;
	}


}
