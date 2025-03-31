package com.greentin.enovation.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Rewards {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String giftCode;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="branch_id")
	private BranchMaster branch;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="org_id")
	private OrganizationMaster org;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cat_id")
	private RewardCategoryMaster category;
	
	private String description;

	@Transient
	private MultipartFile [] rewardImage;

	@Transient
	private String giftCoupen;
	
	@Transient
	private int rCatId;

	private int points;
	
	private int amount;

	@CreationTimestamp
	private Timestamp createdDate;

	@UpdateTimestamp
	private Timestamp updatedDate;
	
	private String url;
	
	private int isEnable;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGiftCode() {
		return giftCode;
	}
	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public MultipartFile[] getRewardImage() {
		return rewardImage;
	}
	public void setRewardImage(MultipartFile[] rewardImage) {
		this.rewardImage = rewardImage;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}

		public Rewards() {
		super();
	}
	public Rewards(int id) {
		super();
		this.id = id;
	}
	public BranchMaster getBranch() {
		return branch;
	}
	public void setBranch(BranchMaster branch) {
		this.branch = branch;
	}
	public OrganizationMaster getOrg() {
		return org;
	}
	public void setOrg(OrganizationMaster org) {
		this.org = org;
	}
	public String getGiftCoupen() {
		return giftCoupen;
	}
	public void setGiftCoupen(String giftCoupen) {
		this.giftCoupen = giftCoupen;
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
	public String getUrl() {
		if(url==null) {
			return url;	
		}else {
			return EnovationConfig.buddyConfig.get("ProfilePicPathUrl")+url;
		}
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public RewardCategoryMaster getCategory() {
		return category;
	}
	public void setCategory(RewardCategoryMaster category) {
		this.category = category;
	}
	public int getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(int isEnable) {
		this.isEnable = isEnable;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getrCatId() {
		return rCatId;
	}
	public void setrCatId(int rCatId) {
		this.rCatId = rCatId;
	}

	
}
