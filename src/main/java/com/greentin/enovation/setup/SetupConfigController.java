package com.greentin.enovation.setup;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.greentin.enovation.accesscontrol.ProductOrgConfig;
import com.greentin.enovation.accesscontrol.UserResponse;
import com.greentin.enovation.model.BranchSetupConfig;
import com.greentin.enovation.model.NoticeSetup;
import com.greentin.enovation.model.SetupMaster;
import com.greentin.enovation.model.SocialMedia;
import com.greentin.enovation.model.ThresholdSetup;
import com.greentin.enovation.response.Response;


@RestController
public class SetupConfigController {

	@Autowired
	private SetupConfigService setupconfigservice;
	
	@CrossOrigin
	@PostMapping(value="/addsetupmaster")
	public Response saveSetupMaster(@RequestBody SetupMaster setupmaster) {
		return setupconfigservice.saveSetupMaster(setupmaster);		
	}
	
	@CrossOrigin
	@PostMapping(value="/addbranchSetupConfig")
	public Response savebranchSetupConfig(@RequestBody BranchSetupConfig branchSetupConfig) {
		return setupconfigservice.saveBranchSetupConfig(branchSetupConfig);	
	}
	
	@CrossOrigin
	@PostMapping(value="/updatesetupmaster")
	public Response updateSetupMaster(@RequestBody SetupMaster sm) {
		return setupconfigservice.updateSetupMaster(sm);	
	}
	
	@CrossOrigin
	@PostMapping(value="/updatebranchSetupConfig")
	public Response updatebranchSetupConfig(@RequestBody BranchSetupConfig bs) {
		return setupconfigservice.updateBranchSetupConfig(bs);	
	}
	
	@CrossOrigin
	@GetMapping(value="/getconfig/{bid}")
	public Response getsetupconfig(@PathVariable("bid") Integer bid) {
		return setupconfigservice.listofSidemenu(bid);
	}
	
	@CrossOrigin
	@PostMapping(value="/updateActivitySetup")
	public Response updateActivitySetup(@RequestBody ProductOrgConfig poc) {
		return setupconfigservice.updateActivitySetup(poc);	
	}
	
	@CrossOrigin
	@PostMapping(value="/updateDocChangeSetup")
	public Response updateDocChangeSetup(@RequestBody ProductOrgConfig poc) {
		return setupconfigservice.updateDocChangeSetup(poc);	
	}
	
	@CrossOrigin
	@PostMapping(value="/isBeforeUploadImageOn")
	public Response uploadImage(@RequestBody ProductOrgConfig poc) {
		return setupconfigservice.uploadImage(poc);	
	}
	
	@CrossOrigin
	@PostMapping(value="/isAfterUploadImageOn")
	public Response afterUploadImage(@RequestBody ProductOrgConfig poc) {
		return setupconfigservice.afterUploadImage(poc);	
	}
	
	@CrossOrigin
	@GetMapping(value="/getProductOrgConfigData/{bid}")
	public Response listOfProdOrgCofig(@PathVariable("bid") Integer bid) {
		return setupconfigservice.listOfProdOrgCofig(bid);
	}
	
	@CrossOrigin
	@PostMapping(value="/setThreshold")
	public Response setThreshold(@RequestBody ThresholdSetup thSetup) {
		return setupconfigservice.setThreshold(thSetup);	
	}
	
	@CrossOrigin
	@GetMapping(value="/getThresholdDetails/{branchId}/{deptId}")
	public Response getThresholdDetails(@PathVariable("branchId") Integer branchId,@PathVariable("deptId") Integer deptId) {
		return setupconfigservice.getThresholdDetails(branchId,deptId);
	}

	@CrossOrigin
	@PostMapping(value="/updateThreshold")
	public Response updateThreshold(@RequestBody ThresholdSetup thSetup) {
		return setupconfigservice.updateThreshold(thSetup);	
	}
	
	@CrossOrigin
	@PostMapping(value="/deleteThreshold")
	public Response deleteThreshold(@RequestBody ThresholdSetup thSetup) {
		return setupconfigservice.deleteThreshold(thSetup);	
	}
	
	
	@CrossOrigin
	@GetMapping(value="/getSocialMediaDetails/{branchId}")
	public Response getSocialMediaDetails(@PathVariable("branchId") Integer branchId) {
		return setupconfigservice.getSocialMediaDetails(branchId);
	}
	
	@CrossOrigin
	@PostMapping(value="/addSocialMedia")
	public Response addSocialMedia(@RequestBody SocialMedia socialMedia) {
		return setupconfigservice.addSocialMedia(socialMedia);	
	}
	
	@CrossOrigin
	@PostMapping(value="/updateSocialMedia")
	public Response updateSocialMedia(@RequestBody SocialMedia socialMedia) {
		return setupconfigservice.updateSocialMedia(socialMedia);	
	}
	
	@CrossOrigin
	@PostMapping(value="/deleteSocialMedia")
	public Response deleteSocialMedia(@RequestBody SocialMedia socialMedia) {
		return setupconfigservice.deleteSocialMedia(socialMedia);	
	}
	
	@CrossOrigin
	@PostMapping(value="/addNotice")
	public Response addNotice(@RequestBody NoticeSetup notice) {
		return setupconfigservice.addNotice(notice);	
	}
	
	@CrossOrigin
	@GetMapping(value="/getNoticeDetails/{branchId}")
	public Response getNoticeDetails(@PathVariable("branchId") Integer branchId) {
		return setupconfigservice.getNoticeDetails(branchId);
	}

	@CrossOrigin
	@PostMapping(value="/deleteNotice")
	public Response deleteNotice(@RequestBody NoticeSetup notice) {
		return setupconfigservice.deleteNotice(notice);	
	}
	
	
	@CrossOrigin
	@PostMapping(value="/updateHappinessSetup")
	public Response updateHappinessSetup(@RequestBody ProductOrgConfig poc) {
		return setupconfigservice.updateHappinessSetup(poc);	
	}
	
	@CrossOrigin
	@PostMapping(value="/updateMandatoryFieldDetails")
	public Response updateMandatoryFieldDetails(@RequestBody ProductOrgConfig poc) {
		System.out.println("Inside updateMandatoryFieldDetails");
		return setupconfigservice.updateMandatoryFieldDetails(poc);	
	}

	@CrossOrigin
	@PostMapping(value="/deleteOjtRegistration/{ojtId}")
	public Response deleteOjtRegistration(@PathVariable("ojtId") Integer ojtId) {		return setupconfigservice.deleteOjtRegistration(ojtId);
	}
	
}
