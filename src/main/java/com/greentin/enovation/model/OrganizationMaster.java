package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.web.multipart.MultipartFile;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.utils.EnovationConstants;

@Entity
@Table(name="master_organization")
//@javax.persistence.Cacheable
public class OrganizationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "org_id", unique = true, nullable = false)
	private int orgId;

	private String name;

	private String orgDomain;

	private String alies;

	private String portalLink;
	
	private String shortPortalLink;

	private String serverDomainName;

	private Timestamp createdDate;

	private Timestamp updatedDate;

	@Column
	private String logo;

	@Column(name = "registered_add", length = 2000)
	private String registeredAdd;

	@Transient
	private MultipartFile orgLogo;

	/*
	 * @OneToMany(mappedBy="org")
	 * 
	 * @JsonManagedReference private Set<ProductOrgConfig> productOrgConfigList;
	 */

	public OrganizationMaster() {
		super();
	}

	public OrganizationMaster(String logo, String name) {
		super();
		this.logo = logo;
		this.name = name;
	}

	public OrganizationMaster(int orgId) {
		super();
		this.orgId = orgId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getLogo() {
		if (logo != null)
			return EnovationConfig.buddyConfig.get(EnovationConstants.PROFILEPIC_PATH_URL) + logo;
		return null;
	}

	public String getLogoPath() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public MultipartFile getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(MultipartFile orgLogo) {
		this.orgLogo = orgLogo;
	}

	/*
	 * public Set<ProductOrgConfig> getProductOrgConfigList() { return
	 * productOrgConfigList; }
	 * 
	 * public void setProductOrgConfigList(Set<ProductOrgConfig>
	 * productOrgConfigList) { this.productOrgConfigList = productOrgConfigList; }
	 */

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

	public String getPortalLink() {
		return portalLink;
	}

	public void setPortalLink(String portalLink) {
		this.portalLink = portalLink;
	}

	public String getServerDomainName() {
		return serverDomainName;
	}

	public void setServerDomainName(String serverDomainName) {
		this.serverDomainName = serverDomainName;
	}

	public String getRegisteredAdd() {
		return registeredAdd;
	}

	public void setRegisteredAdd(String registeredAdd) {
		this.registeredAdd = registeredAdd;
	}

	public String getShortPortalLink() {
		return shortPortalLink;
	}

	public void setShortPortalLink(String shortPortalLink) {
		this.shortPortalLink = shortPortalLink;
	}
	

	/*
	 * public Set<Product> getProductList() { return productList; }
	 * 
	 * public void setProductList(Set<Product> productList) { this.productList =
	 * productList; }
	 */

}
