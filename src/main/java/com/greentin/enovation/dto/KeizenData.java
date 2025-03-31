package com.greentin.enovation.dto;

import java.util.ArrayList;
import java.util.List;

import com.greentin.enovation.utils.OwnerDataSet;

public class KeizenData {

	private String imageUrl;
	private String benifits;
	private String results;
	private String analysis;
	private String kFinish;
	private String kStart;
	private String meanTimeInDays;
	private String category;
	private String counterMeasure;
	private String problemStatus;
	private String kaizenIdea;
	private String suggestionTitle;
	private String suggestionNumber;
	private String implementationTeam;
	private String evalutionTeam;
	//private String target;
	private String empImage;
	private String department;
	private String initiator;
	private String controller;

	private String implUrl;
	private String emplUrl;
	private String orgLogo;
	private String ownerName;
	private String documentDetails;
	private String docName;
	private String fileName;
	private int branchId;
	private String content;
	private long id;

	private List<OwnerDataSet> ownerDetails = new ArrayList<>();

	private boolean KK,JH,QM,PM,SH,OTPM,DM,ET,P,Q,C,D,S,M;


	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBenifits() {
		return benifits;
	}
	public void setBenifits(String benifits) {
		this.benifits = benifits;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getAnalysis() {
		return analysis;
	}
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	public String getkFinish() {
		return kFinish;
	}
	public void setkFinish(String kFinish) {
		this.kFinish = kFinish;
	}
	public String getkStart() {
		return kStart;
	}
	public void setkStart(String kStart) {
		this.kStart = kStart;
	}
	public String getCounterMeasure() {
		return counterMeasure;
	}
	public void setCounterMeasure(String counterMeasure) {
		this.counterMeasure = counterMeasure;
	}
	public String getProblemStatus() {
		return problemStatus;
	}
	public void setProblemStatus(String problemStatus) {
		this.problemStatus = problemStatus;
	}
	public String getKaizenIdea() {
		return kaizenIdea;
	}
	public void setKaizenIdea(String kaizenIdea) {
		this.kaizenIdea = kaizenIdea;
	}
	public String getSuggestionTitle() {
		return suggestionTitle;
	}
	public void setSuggestionTitle(String suggestionTitle) {
		this.suggestionTitle = suggestionTitle;
	}
	public String getSuggestionNumber() {
		return suggestionNumber;
	}
	public void setSuggestionNumber(String suggestionNumber) {
		this.suggestionNumber = suggestionNumber;
	}
	public String getImplementationTeam() {
		return implementationTeam;
	}
	public void setImplementationTeam(String implementationTeam) {
		this.implementationTeam = implementationTeam;
	}
	public String getEvalutionTeam() {
		return evalutionTeam;
	}
	public void setEvalutionTeam(String evalutionTeam) {
		this.evalutionTeam = evalutionTeam;
	}

	public String getEmpImage() {
		return empImage;
	}
	public void setEmpImage(String empImage) {
		this.empImage = empImage;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}

	public String getMeanTimeInDays() {
		return meanTimeInDays;
	}
	public void setMeanTimeInDays(String meanTimeInDays) {
		this.meanTimeInDays = meanTimeInDays;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImplUrl() {
		return implUrl;
	}
	public void setImplUrl(String implUrl) {
		this.implUrl = implUrl;
	}
	public String getEmplUrl() {
		return emplUrl;
	}
	public void setEmplUrl(String emplUrl) {
		this.emplUrl = emplUrl;
	}
	public boolean getKK() {
		return KK;
	}
	public void setKK(boolean kK) {
		KK = kK;
	}
	public boolean getJH() {
		return JH;
	}
	public void setJH(boolean jH) {
		JH = jH;
	}
	public boolean getQM() {
		return QM;
	}
	public void setQM(boolean qM) {
		QM = qM;
	}
	public boolean getPM() {
		return PM;
	}
	public void setPM(boolean pM) {
		PM = pM;
	}
	public boolean getSH() {
		return SH;
	}
	public void setSH(boolean sH) {
		SH = sH;
	}
	public boolean getOTPM() {
		return OTPM;
	}
	public void setOTPM(boolean oTPM) {
		OTPM = oTPM;
	}
	public boolean getDM() {
		return DM;
	}
	public void setDM(boolean dM) {
		DM = dM;
	}
	public boolean getET() {
		return ET;
	}
	public void setET(boolean eT) {
		ET = eT;
	}
	public boolean getP() {
		return P;
	}
	public void setP(boolean p) {
		P = p;
	}
	public boolean getQ() {
		return Q;
	}
	public void setQ(boolean q) {
		Q = q;
	}
	public boolean getC() {
		return C;
	}
	public void setC(boolean c) {
		C = c;
	}
	public boolean getD() {
		return D;
	}
	public void setD(boolean d) {
		D = d;
	}
	public boolean getS() {
		return S;
	}
	public void setS(boolean s) {
		S = s;
	}
	public boolean getM() {
		return M;
	}
	public void setM(boolean m) {
		M = m;
	}
	public String getOrgLogo() {
		return orgLogo;
	}
	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getDocumentDetails() {
		return documentDetails;
	}
	public void setDocumentDetails(String documentDetails) {
		this.documentDetails = documentDetails;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<OwnerDataSet> getOwnerDetails() {
		return ownerDetails;
	}
	public void setOwnerDetails(List<OwnerDataSet> ownerDetails) {
		this.ownerDetails = ownerDetails;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


}
