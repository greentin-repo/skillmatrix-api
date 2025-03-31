package com.greentin.enovation.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name ="sugg_flow_config")
public class SuggestionFlowConfig {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long flowConfId;
	
	private int branchId;
	
	private int orgId;
	
	private int createdBy;
	
	private int updatedBy;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp updatedDate;
	
	
	// Config parameter - Create Suggestion
	
	@Column(name = "is_dept_enabled", length = 50)
	private String isSuggDeptEnbld;
	
	@Column(name = "is_line_enabled", length = 50)
	private String isSuggLineEnbld;
	
	@Column(name = "is_cat_enabled", length = 50)
	private String isSuggCatEnbld;
	
	@Column(name = "is_sugg_src_enabled", length = 50)
	private String isSuggSrcEnbld;
	
	@Column(name = "par_enbld_for_initiator", length = 50)
	private String parEnbledForInitiator;
	
	
	// Config parameter - Suggestion Flow
	
	@Column(name = "is_cntrl_enabled", length = 50,  columnDefinition = "varchar(255) default 'Y'")
	private String isCntrlEnbld;
	
	@Column(name = "is_eval_enabled", length = 50)
	private String isEvalEnbld;
	
	@Column(name = "is_impl_enabled", length = 50, columnDefinition = "varchar(255) default 'Y'")
	private String isImplEnbld;
	
	@Column(name = "is_assess_enabled", length = 50)
	private String isAssessEnbld;
	
	// Config parameter - Evaluator
	
	@Column(name = "is_chg_doc_enabled", length = 50)
	private String isChgDocEnbld;
	
	@Column(name = "is_kaizen_act_enabled", length = 50)
	private String isKaizenActEnbld;
	
	@Column(name = "is_svg_type_enabled", length = 50)
	private String isSavingTypeEnbld;
	
	@Column(name = "is_particulars_enabled", length = 50)
	private String isParticularsEnbld;
	
	@Column(name = "is_horiz_dplymnt_enabled", length = 50)
	private String isHorizDplymntsEnbld;
	
	@Column(name = "is_fin_aprvl_enabled", length = 50)
	private String isFinAprvlEnbld;
	
	@Column(name = "kaizen_check_req_eval", length = 50)
	private String kaizenCheckReqEval;
	
	// Config parameter - Implementor
	
	@Column(name = "is_benefits_enabled", length = 50)
	private String isBenefitsEnbld;
	
	@Column(name = "is_impl_act_enabled", length = 50)
	private String isImplActionEnbld;
	
	@Column(name = "is_sugg_on_hold_enabled", length = 50)
	private String isSuggOnHoldEnbld;
	
	@Column(name = "is_sugg_reject_enabled", length = 50)
	private String isSuggRejectEnbld;
	
	@Column(name = "is_fin_verif_enabled", length = 50)
	private String isFinVerifEnbld;
	
	@Column(name = "is_impl_horiz_dplymnt_enabled", length = 50)
	private String isImplHorizDplymntEnbld;
	
	
	@Column(name = "is_sugg_flow_setup_compltd", length = 50)
	private String isSuggFlowSetupcmpltd;
	
	@Column(name = "kaizen_check_req_impl", length = 50)
	private String kaizenCheckReqImpl;
	
	@Column(name = "is_impl_particulars_enbld", length = 50)
	private String isImplParticularsEnbld;
	
	
	public SuggestionFlowConfig() {
		super();
	}

	public SuggestionFlowConfig(long flowConfId) {
		super();
		this.flowConfId = flowConfId;
	}

	public long getFlowConfId() {
		return flowConfId;
	}

	public void setFlowConfId(long flowConfId) {
		this.flowConfId = flowConfId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getIsSuggDeptEnbld() {
		return isSuggDeptEnbld;
	}

	public void setIsSuggDeptEnbld(String isSuggDeptEnbld) {
		this.isSuggDeptEnbld = isSuggDeptEnbld;
	}

	public String getIsSuggLineEnbld() {
		return isSuggLineEnbld;
	}

	public void setIsSuggLineEnbld(String isSuggLineEnbld) {
		this.isSuggLineEnbld = isSuggLineEnbld;
	}

	public String getIsSuggCatEnbld() {
		return isSuggCatEnbld;
	}

	public void setIsSuggCatEnbld(String isSuggCatEnbld) {
		this.isSuggCatEnbld = isSuggCatEnbld;
	}

	public String getIsSuggSrcEnbld() {
		return isSuggSrcEnbld;
	}

	public void setIsSuggSrcEnbld(String isSuggSrcEnbld) {
		this.isSuggSrcEnbld = isSuggSrcEnbld;
	}

	public String getIsCntrlEnbld() {
		return isCntrlEnbld;
	}

	public void setIsCntrlEnbld(String isCntrlEnbld) {
		this.isCntrlEnbld = isCntrlEnbld;
	}

	public String getIsEvalEnbld() {
		return isEvalEnbld;
	}

	public void setIsEvalEnbld(String isEvalEnbld) {
		this.isEvalEnbld = isEvalEnbld;
	}

	public String getIsImplEnbld() {
		return isImplEnbld;
	}

	public void setIsImplEnbld(String isImplEnbld) {
		this.isImplEnbld = isImplEnbld;
	}

	public String getIsAssessEnbld() {
		return isAssessEnbld;
	}

	public void setIsAssessEnbld(String isAssessEnbld) {
		this.isAssessEnbld = isAssessEnbld;
	}

	public String getIsChgDocEnbld() {
		return isChgDocEnbld;
	}

	public void setIsChgDocEnbld(String isChgDocEnbld) {
		this.isChgDocEnbld = isChgDocEnbld;
	}

	public String getIsKaizenActEnbld() {
		return isKaizenActEnbld;
	}

	public void setIsKaizenActEnbld(String isKaizenActEnbld) {
		this.isKaizenActEnbld = isKaizenActEnbld;
	}

	public String getIsSavingTypeEnbld() {
		return isSavingTypeEnbld;
	}

	public void setIsSavingTypeEnbld(String isSavingTypeEnbld) {
		this.isSavingTypeEnbld = isSavingTypeEnbld;
	}

	public String getIsParticularsEnbld() {
		return isParticularsEnbld;
	}

	public void setIsParticularsEnbld(String isParticularsEnbld) {
		this.isParticularsEnbld = isParticularsEnbld;
	}

	public String getIsHorizDplymntsEnbld() {
		return isHorizDplymntsEnbld;
	}

	public void setIsHorizDplymntsEnbld(String isHorizDplymntsEnbld) {
		this.isHorizDplymntsEnbld = isHorizDplymntsEnbld;
	}

	public String getIsFinAprvlEnbld() {
		return isFinAprvlEnbld;
	}

	public void setIsFinAprvlEnbld(String isFinAprvlEnbld) {
		this.isFinAprvlEnbld = isFinAprvlEnbld;
	}

	public String getIsBenefitsEnbld() {
		return isBenefitsEnbld;
	}

	public void setIsBenefitsEnbld(String isBenefitsEnbld) {
		this.isBenefitsEnbld = isBenefitsEnbld;
	}

	public String getIsImplActionEnbld() {
		return isImplActionEnbld;
	}

	public void setIsImplActionEnbld(String isImplActionEnbld) {
		this.isImplActionEnbld = isImplActionEnbld;
	}

	public String getIsSuggOnHoldEnbld() {
		return isSuggOnHoldEnbld;
	}

	public void setIsSuggOnHoldEnbld(String isSuggOnHoldEnbld) {
		this.isSuggOnHoldEnbld = isSuggOnHoldEnbld;
	}

	public String getIsSuggRejectEnbld() {
		return isSuggRejectEnbld;
	}

	public void setIsSuggRejectEnbld(String isSuggRejectEnbld) {
		this.isSuggRejectEnbld = isSuggRejectEnbld;
	}

	public String getIsFinVerifEnbld() {
		return isFinVerifEnbld;
	}

	public void setIsFinVerifEnbld(String isFinVerifEnbld) {
		this.isFinVerifEnbld = isFinVerifEnbld;
	}

	public String getIsImplHorizDplymntEnbld() {
		return isImplHorizDplymntEnbld;
	}

	public void setIsImplHorizDplymntEnbld(String isImplHorizDplymntEnbld) {
		this.isImplHorizDplymntEnbld = isImplHorizDplymntEnbld;
	}

	public String getIsSuggFlowSetupcmpltd() {
		return isSuggFlowSetupcmpltd;
	}

	public void setIsSuggFlowSetupcmpltd(String isSuggFlowSetupcmpltd) {
		this.isSuggFlowSetupcmpltd = isSuggFlowSetupcmpltd;
	}

	public String getParEnbledForInitiator() {
		return parEnbledForInitiator;
	}

	public void setParEnbledForInitiator(String parEnbledForInitiator) {
		this.parEnbledForInitiator = parEnbledForInitiator;
	}

	public String getKaizenCheckReqEval() {
		return kaizenCheckReqEval;
	}

	public void setKaizenCheckReqEval(String kaizenCheckReqEval) {
		this.kaizenCheckReqEval = kaizenCheckReqEval;
	}

	public String getKaizenCheckReqImpl() {
		return kaizenCheckReqImpl;
	}

	public void setKaizenCheckReqImpl(String kaizenCheckReqImpl) {
		this.kaizenCheckReqImpl = kaizenCheckReqImpl;
	}

	public String getIsImplParticularsEnbld() {
		return isImplParticularsEnbld;
	}

	public void setIsImplParticularsEnbld(String isImplParticularsEnbld) {
		this.isImplParticularsEnbld = isImplParticularsEnbld;
	}	
}


