package com.greentin.enovation.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;

public class WriteFilesUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(WriteFilesUtils.class);

	private static String  generateUniqueFileName() {
	    String filename = "";
	    long millis = System.currentTimeMillis();
	    String datetime = new Date().toString();
	    datetime = datetime.replace(" ", "");
	    datetime = datetime.replace(":", "");
	    String rndchars = RandomStringUtils.randomAlphanumeric(16);
	    filename = rndchars + "_" + datetime + "_" + millis;
	    return filename;
	}
	
	public static String generateFileNameNDwriteFile(MultipartFile file,String docDirctory, String filePathToTrim ){
		LOGGER.info("IN Dao | generateFileNameNDwriteFile ");
		String filepathcut=null;
		try{
			if(file!=null)  {

				String  filename=generateUniqueFileName() + "."+FilenameUtils.getExtension(file.getOriginalFilename());
				System.out.println("----------Generated File Name------------"+filename);
				String directory =docDirctory;
				System.out.println("doc dir :"+docDirctory);
				File dire = new File(directory);
				if (!dire.exists()){dire.mkdirs();}
				String doc_path = Paths.get(directory, filename).toString();
				/*
				 * THIS STRING TO BE TRIM  FOR SAVE IMAGE IN DB
				 * */
				String trim_path = Paths.get(filePathToTrim, filename).toString();
				LOGGER.info("File path for Write file On Server"+doc_path);
				filepathcut=(trim_path).replace("\\", "/");
				LOGGER.info("File path for store in DB"+filepathcut);
				/*
				 *  Save the file locally
				 */				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(doc_path)));
				stream.write(file.getBytes());
				stream.close();
			}
		}catch(Exception e) {
			LOGGER.info("INSIDE EXCEPTION OCCURRED IN WRITE FILE ON SERVER :"+e.getMessage());
		}

		return filepathcut;
	}
	
	public static String writeFileOnServer(MultipartFile file,String docDirctory, String filePathToTrim ){
		String filepathcut=null;
		try{
			if(file!=null)  {
				String filenameOriginal = file.getOriginalFilename().replaceAll(" ", "");
				String  filename=filenameOriginal;
				String directory =docDirctory;
				System.out.println("doc dir :"+docDirctory);
				File dire = new File(directory);
				if (!dire.exists()){dire.mkdirs();}
				String doc_path = Paths.get(directory, filename).toString();
				/*
				 * THIS STRING TO BE TRIM  FOR SAVE IMAGE IN DB
				 * */
				String trim_path = Paths.get(filePathToTrim, filename).toString();
				LOGGER.info("File path for Write file On Server"+doc_path);
				filepathcut=(trim_path).replace("\\", "/");
				LOGGER.info("File path for store in DB"+filepathcut);
				/*
				 *  Save the file locally
				 */				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(doc_path)));
				stream.write(file.getBytes());
				stream.close();
			}
		}catch(Exception e) {
			LOGGER.info("INSIDE EXCEPTION OCCURRED IN WRITE FILE ON SERVER :"+e.getMessage());
		}

		return filepathcut;
	}
	public static String updateWriteFileOnServer(MultipartFile file,String docDirctory ,String prevFilePath, String fileToTrim){
		String filepathcut=null;
		try{
			if(file!=null)  {
				
				if(prevFilePath!=null) {
					File fdelete = new File(prevFilePath);
					if (fdelete.exists()) {
						if (fdelete.delete()) {
							LOGGER.info("file Deleted ");
						} else {
							LOGGER.info("file not Deleted ");
						}
					}	
				}
				
				String filenameOriginal = file.getOriginalFilename();
				String  filename=filenameOriginal;
				String directory =docDirctory;
				LOGGER.info("File path for Write file On Server : "+directory);
				File dire = new File(directory);
				if (!dire.exists()){dire.mkdirs();}
				String doc_path = Paths.get(directory, filename).toString();
				System.out.println(doc_path);
				/*
				 * THIS STRING TO BE TRIM  FOR SAVE IMAGE IN DB
				 * */
				String trim_path = Paths.get(fileToTrim, filename).toString();
				LOGGER.info("File path for Write file On Server"+doc_path);
				filepathcut=(trim_path).replace("\\", "/");
				LOGGER.info("File path for store in DB : "+filepathcut);
				/*
				 * Save the file locally
				 * */
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(doc_path)));
				stream.write(file.getBytes());
				stream.close();
			}
		}catch(Exception e) {
			LOGGER.info("INSIDE EXCEPTION OCCURRED IN WRITE FILE(UPDATE) ON SERVER :"+e.getMessage());
		}

		return filepathcut;
	}
	public static boolean removeFileFromServer(String filePath){
		boolean flag=false;
		try{
			if(filePath!=null)  {
				File fdelete = new File(filePath);
				LOGGER.info(" fdelete file path "+fdelete);
				if (fdelete.exists()) {
					if (fdelete.delete()) {
						fdelete.delete();
						LOGGER.info("file Deleted ");
						flag=true;
					} else {
						LOGGER.info("file not Deleted ");
					}
				}
			}
		}catch(Exception e) {
			LOGGER.info("Exception Occurred in Write File on server");
			e.printStackTrace();
		}

		return flag;
	}

	public static boolean WriteConfigFileOnServer(String serverDomainName) {
		//Writer writer = null;
		boolean flag=true;
		try {
			String fileContent=(EnovationConfig.buddyConfig.get("masterConfigTemp")).replaceAll("{serverdomain}", serverDomainName);
			/* writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(serverDomainName+".config"), "utf-8"));
		    writer.write(fileContent);
		     writer.close();*/
			File dire =new File("c://temp//"+serverDomainName+".config");
			if (!dire.exists()){
				dire.mkdirs();
				 if (dire.createNewFile()){
		                System.out.println("File is created!");
		            } else {
		                System.out.println("File already exists.");
		            }
			}
			 
			FileWriter stream = new FileWriter(dire);
			stream.write(fileContent.toString());
			stream.close();
			flag=true;

		} catch (IOException ex) {
			LOGGER.info("INSIDE EXCEPTION IN WriteConfigFileOnServer METHOD: "+ex.getMessage());
		} finally {
			LOGGER.info("INSIDE IN FINALLY BLOOCK(WriteConfigFileOnServer)");
		}
		return flag;
	}


}
