package com.greentin.enovation.setup;



import java.util.List;

import com.greentin.enovation.model.skillMatrix.SMOJTRegis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SetupData;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;
import com.greentin.enovation.response.Response;
import com.greentin.enovation.utils.EnovationConstants;


@Service
public class SetupConfigSerImp implements SetupConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetupConfigSerImp.class);
	
	@Autowired
	private SetupConfigDao setupconfigDao;

	@Override
	public Response saveSetupMaster(SetupMaster setupMaster) {
		Response response = new Response();
		boolean result = setupconfigDao.saveSetupMaster(setupMaster);
		if (result) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateSetupMaster(SetupMaster s) {
		Response response = new Response();
		boolean result = setupconfigDao.updateSetupMaster(s);
		if (result) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response saveBranchSetupConfig(BranchSetupConfig branchSetupConfig) {
		// TODO Auto-generated method stub
		Response response = new Response();
		boolean result = setupconfigDao.saveBranchSetupConfig(branchSetupConfig);
		if (result) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateBranchSetupConfig(BranchSetupConfig c) {
		Response response = new Response();
		boolean result = setupconfigDao.updateBranchSetupConfig(c);
		if (result) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response listofSidemenu(Integer branchId) {
		Response response = new Response();
		List<SetupData> setupData = setupconfigDao.listofSideMenu(branchId);
		if (setupData != null) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setData(setupData);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}
	

	public Response listofSetupConfig() {
		Response response = new Response();
		try{
			List<SetupMaster> setupmaster = setupconfigDao.listofSetupConfig();
		if (setupmaster != null && setupmaster.size()>0) {
			response.setReason("Saved in SetupMaster");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setSetupmasterList(setupmaster);
		} else {
			response.setReason("Failed to Save in SetupMaster");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code200);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public Response updateActivitySetup(ProductOrgConfig poc) {
		Response response = new Response();
		boolean result = setupconfigDao.updateActivitySetup(poc);
		if (result) {
			response.setReason("Saved in updateActivitySetup");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in updateActivitySetup");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateDocChangeSetup(ProductOrgConfig poc) {
		Response response = new Response();
		boolean result = setupconfigDao.updateDocChangeSetup(poc);
		if (result) {
			response.setReason("Saved in updateDocChangeSetup");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in updateDocChangeSetup");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response uploadImage(ProductOrgConfig poc) {
		Response response = new Response();
		boolean result = setupconfigDao.uploadImage(poc);
		if (result) {
			response.setReason("Saved in isUploadImageOn");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in isUploadImageOn");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response afterUploadImage(ProductOrgConfig poc) {
		Response response = new Response();
		boolean result = setupconfigDao.afterUploadImage(poc);
		if (result) {
			response.setReason("Saved in isUploadImageOn");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in isUploadImageOn");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response listOfProdOrgCofig(Integer branchId) {
		Response response = new Response();
		List<ProductOrgConfig> prodOrgConfigData = setupconfigDao.listOfProdOrgCofig(branchId);
		if (!prodOrgConfigData.isEmpty()) {
			response.setReason("Fetched from ProductOrgConfig");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setProductorglist(prodOrgConfigData);
			
		} else {
			response.setReason("Failed to fetch in ProductOrgConfig");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response setThreshold(ThresholdSetup thSetup) {
		
		Response response = new Response();
		boolean result = setupconfigDao.setThreshold(thSetup);
		if(result) {			
			response.setReason("Threshold saved successfully");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);
			response.setResult(EnovationConstants.resultTrue);
			
		}else {
			response.setReason("Failed to save Threshold");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);	
		}
		return response;
	}

	@Override
	public Response getThresholdDetails(Integer branchId,Integer dpetId) {
		
		Response response = new Response();
		List<ThresholdSetup> resultList = setupconfigDao.getThresholdList(branchId,dpetId);
		
		if (resultList != null) {
			response.setReason("Fetched from Threshold Details");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setThSetup(resultList);
			
		} else {
			response.setReason("Failed to fetch from Threshold Details");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateThreshold(ThresholdSetup thSetup) {
		
		Response response = new Response();
		int count = setupconfigDao.updateThreshold(thSetup);
		
		if(count > 0) {
			response.setReason("Saved in updateThresholdSetup");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORD_UPDATED);
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to Save in updateThresholdSetup");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		
		return response;
	}
	
	@Override
	public Response deleteThreshold(ThresholdSetup thSetup) {
		
		Response response = new Response();
		try{
			int count = setupconfigDao.deleteThreshold(thSetup);
			if(count > 0) {
				response.setReason("Deleted from ThresholdSetup");
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);	
				response.setReason(EnovationConstants.RECORD_DELETED);
				response.setResult(EnovationConstants.resultTrue);
			} else {
				response.setReason("Failed to Delete from ThresholdSetup");
				response.setResult(EnovationConstants.ResultFalse);
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
			}
		}catch (Exception e) {
			
			LOGGER.info("# EXCEPTION INSIDE DELETE THRESHOLD SERVICE" + e.getMessage());
		}
		
		return response;
	}

	@Override
	public Response addSocialMedia(SocialMedia socialMedia) {
		
		Response response = new Response();
		boolean isMediaExist = setupconfigDao.isSocialMediaExist(socialMedia);
		if(isMediaExist){
			response.setReason("Social Media Alreayd Exist");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}else {
			
			boolean result = setupconfigDao.addSocialMedia(socialMedia);
			if(result) {
				response.setReason("Social Media saved successfully");
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.resultTrue);
			}else {
				response.setReason("Failed to save Social Media");
				response.setResult(EnovationConstants.ResultFalse);
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
			}
			
		}
		
		return response;
	}

	@Override
	public Response getSocialMediaDetails(Integer branchId) {
		
		Response response = new Response();
		List<SocialMedia> list = setupconfigDao.getSocialMediaDetails(branchId);
		if(list != null && !list.isEmpty()) {
			response.setReason("Fetched from Social Media Details");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setSocialMedia(list);
			
		} else {
			response.setReason("Failed to fetch from Social Media Details");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateSocialMedia(SocialMedia socialMedia) {
		
		Response response = new Response();
		int count = setupconfigDao.updateSocialMedia(socialMedia);
		if(count > 0) {
			response.setReason("Saved in updateSocialMedia");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORD_UPDATED);
			response.setResult(EnovationConstants.resultTrue);
			
		}else {
			response.setReason("Failed to Save in updateSocialMedia");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);		
		}
		
		return response;
	}

	@Override
	public Response deleteSocialMedia(SocialMedia socialMedia) {
		Response response = new Response();
		boolean result = setupconfigDao.deleteSocialMedia(socialMedia);
		if(result) {
			response.setReason("Deleted from SocialMedia");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORD_DELETED);
			response.setResult(EnovationConstants.resultTrue);
			
		}else {
			response.setReason("Failed to delete from updateSocialMedia");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);		
		}
		
		return response;
	}

	@Override
	public Response addNotice(NoticeSetup notice) {
		Response response = new Response();
		boolean result = setupconfigDao.addNotice(notice);
		if(result) {
			response.setReason("Notice saved successfully");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);
			response.setResult(EnovationConstants.resultTrue);
		}else {
			response.setReason("Failed to save Notice");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response getNoticeDetails(Integer branchId) {
		Response response = new Response();
		List<NoticeSetup> list = setupconfigDao.getNoticeDetails(branchId);
		if(list != null && !list.isEmpty()) {
			response.setReason("Fetched from Notice Setup Details");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
			response.setResult(EnovationConstants.resultTrue);
			response.setNoticeSetup(list);
			
		} else {
			response.setReason("Failed to fetch from Notice Setup Details");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		
		return response;
	}
	
	@Override
	public Response deleteNotice(NoticeSetup notice) {
		Response response = new Response();
		boolean result= setupconfigDao.deleteNotice(notice);
		System.out.println("Printing Notice ID : " + notice.getNoticeId());
		if (result) {
			response.setReason("Notice Deleted Successfully");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed To Delete The Notice");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}

	@Override
	public Response updateHappinessSetup(ProductOrgConfig poc) {
		Response response = new Response();
		boolean result = setupconfigDao.updateHappinessSetup(poc);
		if (result) {
			response.setReason("Happiness Setup updated");
			response.setStatus(EnovationConstants.statusSuccess);
			response.setStatusCode(EnovationConstants.Code200);	
			response.setResult(EnovationConstants.resultTrue);
		} else {
			response.setReason("Failed to update Happiness Setup");
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}
	
	/**
	 * @author Vinay B Jan 8, 2020 2:51:07 PM
	 * @param poc
	 * @return
	 */
	@Override
	public Response updateMandatoryFieldDetails(ProductOrgConfig poc) {
		Response response = new Response();
		if (poc != null && poc.getBranchId() > 0) {
			boolean result = setupconfigDao.updateMandatoryFieldDetails(poc);
			if (result) {
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				response.setResult(EnovationConstants.resultTrue);
			} else {
				response.setResult(EnovationConstants.resultTrue);
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code200);
			}
		} else {
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.CODE400);
			response.setReason(EnovationConstants.BAD_REQUEST);
		}
		return response;
	}

	@Override
	public Response deleteOjtRegistration(Integer ojtId) {
		Response response = new Response();
		try {
			boolean result = setupconfigDao.deleteOjtRegistration(ojtId);
			if (result) {
				response.setReason("OJT Registration deleted successfully");
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.resultTrue);
			} else {
				response.setReason("Failed to delete OJT Registration");
				response.setResult(EnovationConstants.ResultFalse);
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code500);
			}
		} catch (Exception e) {
			LOGGER.error("Error in deleteOjtRegistration: " + e.getMessage());
			response.setReason("Error occurred while deleting OJT Registration: " + e.getMessage());
			response.setResult(EnovationConstants.ResultFalse);
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.Code500);
		}
		return response;
	}
	
}
