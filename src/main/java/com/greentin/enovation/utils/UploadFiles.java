/**
 * @author Vinay B. Jan 7, 2021 2:10:25 PM
 */
package com.greentin.enovation.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;

/**
 * @author Vinay B. Jan 7, 2021 2:10:25 PM
 */

@Component
public class UploadFiles {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFiles.class);

	public static void writeFile(MultipartFile files, String directory) {

		File dir = new File(directory);
		System.out.println("Directory ====> " + dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		try {

			byte[] bytes = files.getBytes();

			System.out.println("File Name ==> " + files.getOriginalFilename());

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath() + File.separator + files.getOriginalFilename());

			System.out.println("Server File ==> " + serverFile);

			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();

			LOGGER.info("Server File Location=" + serverFile.getAbsolutePath());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String generateUniqueFileName() {
		String filename = "";
		long millis = System.currentTimeMillis();
		String datetime = new Date().toString();
		datetime = datetime.replace(" ", "");
		datetime = datetime.replace(":", "");
		String rndchars = RandomStringUtils.randomAlphanumeric(16);
		filename = rndchars + "_" + datetime + "_" + millis;
		return filename;
	}

	public static String generateFileNameNDwriteFile(MultipartFile file, String docDirctory, String filePathToTrim) {
		String filepathcut = null;
		try {
			if (file != null) {

				String filename = generateUniqueFileName() + "."
						+ FilenameUtils.getExtension(file.getOriginalFilename());
				System.out.println("----------Generated File Name------------" + filename);
				String directory = docDirctory;
				System.out.println("doc dir :" + docDirctory);
				File dire = new File(directory);
				if (!dire.exists()) {
					dire.mkdirs();
				}
				String doc_path = Paths.get(directory, filename).toString();
				/*
				 * THIS STRING TO BE TRIM FOR SAVE IMAGE IN DB
				 */
				String trim_path = Paths.get(filePathToTrim, filename).toString();
				LOGGER.info("File path for Write file On Server" + doc_path);
				filepathcut = (trim_path).replace("\\", "/");
				LOGGER.info("File path for store in DB" + filepathcut);
				/*
				 * Save the file locally
				 */
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(doc_path)));
				stream.write(file.getBytes());
				stream.close();
			}
		} catch (Exception e) {
			LOGGER.info("INSIDE EXCEPTION OCCURRED IN WRITE FILE ON SERVER :" + e.getMessage());
		}

		return filepathcut;
	}

}
