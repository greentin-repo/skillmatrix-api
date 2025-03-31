package com.greentin.enovation.utils;

import java.util.Base64;

/*
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;*/
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class EncryptDecryptUtils {

//
// public static String  encrypt(String data) {
// String encryptData=null;
// try {
// /*SimplePBEConfig config = new SimplePBEConfig();
// config.setAlgorithm("PBEWithMD5AndTripleDES");
// config.setKeyObtentionIterations(1000);
// config.setPassword("root");
// StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
// encryptor.setConfig(config);
// encryptor.initialize();
//    encryptData= PropertyValueEncryptionUtils.encrypt(data, encryptor);*/
// BASE64Encoder encoder = new BASE64Encoder();
// encryptData = new String(encoder.encodeBuffer(data.getBytes()));
// }catch(Exception e) {
// e.printStackTrace();
// }
// return encryptData;
// }
// public static String  decrypt(String data) {
// BASE64Decoder decoder = new BASE64Decoder();
// /*SimplePBEConfig config = new SimplePBEConfig();
// config.setAlgorithm("PBEWithMD5AndTripleDES");
// config.setKeyObtentionIterations(1000);
// config.setPassword("propertiesFilePassword");
// StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
// encryptor.setConfig(config);
// encryptor.initialize();
// return PropertyValueEncryptionUtils.decrypt(data, encryptor);*/
// try {
// data = new String(decoder.decodeBuffer(data));
// } catch (IOException e) {
// e.printStackTrace();
// }
// return data;
// }

	public static String encrypt(String data) {

		String encryptData = Base64.getEncoder().encodeToString(data.getBytes());
		return encryptData;
	}

	public static String decrypt(String data) {

		byte[] decodedBytes = Base64.getDecoder().decode(data);
		String decodedString = new String(decodedBytes);
		return decodedString;
	}

}