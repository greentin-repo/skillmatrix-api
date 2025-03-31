package com.greentin.enovation.setup;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.UserResponse;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;
import com.greentin.enovation.response.Response;

public interface SetupConfigService {
	
	public Response saveSetupMaster(SetupMaster setupMaster);
	public Response saveBranchSetupConfig(BranchSetupConfig branchSetupConfig);
	public Response listofSidemenu(Integer branchId);
	public Response updateSetupMaster(SetupMaster s);
	public Response updateBranchSetupConfig(BranchSetupConfig c);
	public Response updateActivitySetup(ProductOrgConfig poc);
	public Response updateDocChangeSetup(ProductOrgConfig poc);
	public Response uploadImage(ProductOrgConfig poc);
	public Response afterUploadImage(ProductOrgConfig poc);
	public Response listOfProdOrgCofig(Integer bid);
	public Response setThreshold(ThresholdSetup thSetup);
	public Response getThresholdDetails(Integer branchId,Integer deptId);
	public Response updateThreshold(ThresholdSetup thSetup);
	public Response addSocialMedia(SocialMedia socialMedia);
	public Response getSocialMediaDetails(Integer branchId);
	public Response updateSocialMedia(SocialMedia socialMedia);
	public Response deleteSocialMedia(SocialMedia socialMedia);
	public Response addNotice(NoticeSetup notice);
	public Response getNoticeDetails(Integer branchId);
	public Response updateHappinessSetup(ProductOrgConfig poc);
	public Response deleteThreshold(ThresholdSetup thSetup);
	public Response deleteNotice(NoticeSetup notice);
	Response updateMandatoryFieldDetails(ProductOrgConfig poc);
	
}
