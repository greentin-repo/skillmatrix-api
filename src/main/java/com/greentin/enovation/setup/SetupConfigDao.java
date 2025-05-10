package com.greentin.enovation.setup;

import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;
import com.greentin.enovation.model.SetupData;
import java.util.List;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.skillMatrix.SMOJTRegis;

public interface SetupConfigDao {
	
	public boolean saveSetupMaster(SetupMaster setupMaster);
	
	public boolean saveBranchSetupConfig(BranchSetupConfig branchSetupConfig);
	
	List<SetupData> listofSideMenu(Integer branchId);
	
	public boolean updateBranchSetupConfig(BranchSetupConfig bconfig);
	
	public boolean updateSetupMaster(SetupMaster setupmaster);
	
	public List<SetupMaster> listofSetupConfig();
	
	public boolean updateActivitySetup(ProductOrgConfig poc);
	
	public boolean updateDocChangeSetup(ProductOrgConfig poc);
	
	public boolean uploadImage(ProductOrgConfig poc);
	
	public boolean afterUploadImage(ProductOrgConfig poc);
	
	public List<ProductOrgConfig> listOfProdOrgCofig(Integer branchId);
	
	public boolean setThreshold(ThresholdSetup thSetup);
	
	public List<ThresholdSetup> getThresholdList(Integer branchId,Integer deptId);
	
	public int updateThreshold(ThresholdSetup thSetup);
	
	public boolean addSocialMedia(SocialMedia socialMedia);
	
	public List<SocialMedia> getSocialMediaDetails(Integer branchId);
	
	public int updateSocialMedia(SocialMedia socialMedia);
	
	public boolean deleteSocialMedia(SocialMedia socialMedia);
	
	public boolean addNotice(NoticeSetup notice);
	
	public List<NoticeSetup> getNoticeDetails(Integer branchId);
	
	public boolean updateHappinessSetup(ProductOrgConfig poc);
	
	public int deleteThreshold(ThresholdSetup thSetup);
	
	//public boolean deleteNotice(NoticeSetup notice);
	boolean deleteNotice(NoticeSetup notice);
	
	public boolean isSocialMediaExist(SocialMedia socialMedia);

	boolean updateMandatoryFieldDetails(ProductOrgConfig poc);

	boolean deleteOjtRegistration(Integer ojtId);

}
