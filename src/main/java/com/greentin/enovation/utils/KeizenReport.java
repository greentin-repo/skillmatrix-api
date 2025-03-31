package com.greentin.enovation.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.greentin.enovation.dto.KeizenData;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;

public class KeizenReport {
	

	@SuppressWarnings({ "unchecked", "unused" })
	public static String downloadForm(String designPathLocal, KeizenData keizenData, String jasperTemplateUrl) throws FileNotFoundException, JRException {
		InputStream inputStream = null;
		if(jasperTemplateUrl!=null) {
			inputStream = new FileInputStream(designPathLocal+jasperTemplateUrl);
				
		}else {
		inputStream = new FileInputStream(designPathLocal+"/keizen_form_new.jrxml");
		}
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(getTestList());
		@SuppressWarnings("rawtypes")
		Map parameters = new HashMap();
		//String imageUrl = null;
		Image implimage = null;
		Image empImage = null;
		Image orgImage = null;
		//System.out.println("impl url :"+keizenData.getImplUrl());
		//System.out.println("empl url :"+keizenData.getEmplUrl());
		//System.out.println("file Path : "+designPathLocal);
		String KK =null;
		String JH = null;
		String QM = null;
		String PM = null;
		String SH = null;
		String OTPM = null;
		String DM = null;
		String ET = null;
		String P = null;
		String Q = null;
		String C = null;
		String D = null;
		String S = null;
		String M = null;
		String ownerName="N/A";
		String documentDetails="N/A";
		String fileName="N/A";
		String docName="NA";


		try {
			//System.out.println("input value of kk : "+keizenData.getKK());
			//System.out.println("value of kk : "+KK);

			if(keizenData.getKK()) { 
				KK = designPathLocal.replace("\\", "/") +"icon1.png";
				//System.out.println("value of kk : "+KK.replace("\\", "/"));
			}


			//System.out.println("after value of kk : "+ClassLoader.getSystemResource("correctTick.png").getPath());
			if(keizenData.getJH()) {
				JH = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getQM()) {
				QM = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getPM()) {
				PM = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getSH()) {
				SH = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getOTPM()) {
				OTPM = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getDM()) {
				DM = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getET()) {
				ET = designPathLocal.replace("\\", "/") +"icon1.png";
			}

			if(keizenData.getP()) {
				P = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getQ()) {
				Q = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getC()) {
				C = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getD()) {
				D = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getS()) {
				S = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getM()) {
				M = designPathLocal.replace("\\", "/") +"icon1.png";
			}
			if(keizenData.getOwnerName()!=null) {
				ownerName = keizenData.getOwnerName();
			}
			if(keizenData.getDocumentDetails()!=null) {
				documentDetails = keizenData.getDocumentDetails();
			}
			if(keizenData.getDocName()!=null) {
				docName = keizenData.getDocName();
			}
			if(keizenData.getFileName()!=null) {
				fileName = keizenData.getFileName();
			}
			//keizenData.setOrgLogo("https://myenovation.com/static-website/images/myenovation.png");
			if(keizenData.getImplUrl()!=null) {
				URL url = new URL(keizenData.getImplUrl());
				implimage = ImageIO.read(url);
			}
			if(keizenData.getEmplUrl()!=null) {
				URL url = new URL(keizenData.getEmplUrl());
				empImage = ImageIO.read(url);
			}
			if(keizenData.getOrgLogo()!=null) {
				URL url = new URL(keizenData.getOrgLogo());
				orgImage = ImageIO.read(url);
			}

		} catch (Exception e) {
			System.out.println("Unable to retrieve Imagae!!!");
		}
		parameters.put("benifits", keizenData.getBenifits());
		parameters.put("results", keizenData.getResults());
		parameters.put("analysis", keizenData.getAnalysis());
		parameters.put("kFinish", keizenData.getkFinish());
		parameters.put("EMP_IMAGE", empImage);
		parameters.put("IMPL_IMAGE", implimage);
		parameters.put("kStart", keizenData.getkStart());
		parameters.put("counterMeasure", keizenData.getCounterMeasure());
		parameters.put("problemStatus", keizenData.getProblemStatus());
		parameters.put("kaizenIdea", keizenData.getKaizenIdea());
		parameters.put("suggestionTitle", keizenData.getSuggestionTitle());
		parameters.put("sId", keizenData.getSuggestionNumber());
		parameters.put("implementationTeam", keizenData.getImplementationTeam());
		parameters.put("evalutionTeam", keizenData.getEvalutionTeam());
		parameters.put("department", keizenData.getDepartment());
		parameters.put("initiator", keizenData.getInitiator());
		parameters.put("controller", keizenData.getController());
		parameters.put("meanTimeInDays", keizenData.getMeanTimeInDays());
		parameters.put("category", keizenData.getCategory());
		parameters.put("KK", KK);
		parameters.put("JH", JH);
		parameters.put("QM", QM);
		parameters.put("PM", PM);
		parameters.put("SH", SH);
		parameters.put("OTPM", OTPM);
		parameters.put("DM", DM);
		parameters.put("ET", ET);
		parameters.put("P", P);
		parameters.put("Q", Q);
		parameters.put("C", C);
		parameters.put("D", D);
		parameters.put("S", S);
		parameters.put("M", M);
		parameters.put("ORG_LOGO", orgImage);
		//parameters.put("ownerName", ownerName);
		//parameters.put("documentDetails", documentDetails);
		parameters.put("fileName", fileName);
		//parameters.put("docName", docName);
      //System.out.println("owner list size is :"+keizenData.getOwnerDetails().size());
      /*for(OwnerDataSet o : keizenData.getOwnerDetails()) {
    	  System.out.println("owner nAME :"+o.getOwnerName());
      }*/
		if(keizenData.getOwnerDetails().size() == 0) {
			 OwnerDataSet t = new OwnerDataSet(" "," "," ");
			 keizenData.getOwnerDetails().add(t);
		 }
		/*List<OwnerDataSet> list = new ArrayList<>();
		 OwnerDataSet ta = new OwnerDataSet("AKSHAY");
		 list.add(ta);
		 parameters.put("list", new JRBeanCollectionDataSource(list));*/
		 parameters.put("ownerList", new JRBeanCollectionDataSource(keizenData.getOwnerDetails()));
		 // load Jasper XML and Return Jasper Design
		 String admitFile =null;
		 try{
			 JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
			 // Compile Jasper Design and return Jasper Report
			 JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
			 admitFile =designPathLocal+EnovationConstants.KAIZEN_FILENAME+keizenData.getSuggestionNumber()+".pdf";
			 JasperExportManager.exportReportToPdfFile(jasperPrint, admitFile);
			 System.out.println("file path :"+admitFile);
		 }catch(Exception e){
			// e.printStackTrace();
		 }
   return admitFile;

	}

	public static ArrayList<TestData> getTestList() {
		ArrayList<TestData> dataBeanList = new ArrayList<TestData>();
		dataBeanList.add(new TestData("Advanced"));
		return dataBeanList;
	}

}
class TestData{

	public String itemNo;


	public TestData() {
		super();
	}


	public TestData(String itemNo) {
		super();
		this.itemNo = itemNo;
	}


	public String getItemNo() {
		return itemNo;
	}


	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}




}

