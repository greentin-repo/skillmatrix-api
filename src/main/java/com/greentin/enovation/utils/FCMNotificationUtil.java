package com.greentin.enovation.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.communication.NotificationConstants;
import com.greentin.enovation.communication.NotificationMessage;
import com.greentin.enovation.config.EnovationConfig;

@Component
public class FCMNotificationUtil {
	final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	
	

	public static void sendMultipleNotification(List<String> putIds2, String title, String message,
			JSONObject notifi_data) {
		try {
			URL url = new URL(FCM_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + EnovationConfig.buddyConfig.get(EnovationConstants.FCM_URL_ONE_SIGNAL_APP_ID_NAME));
			conn.setRequestProperty("Content-Type", "application/json");
			JSONArray regId = new JSONArray();
			JSONObject objData = new JSONObject();
			JSONObject data = new JSONObject();
			JSONObject notif = new JSONObject();
			for (int i = 0; i < putIds2.size(); i++) {
				regId.put(putIds2.get(i));
			}
			data.put("message", notifi_data);
			notif.put("title", title);
			notif.put("body", message);
			objData.put("registration_ids", regId);
			objData.put("data", notifi_data);
			objData.put("notification", notif);
			objData.put("content-available", true);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(objData.toString());
			wr.flush();
			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			if (status != 0) {
				if (status == 200) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
				} else if (status == 401) {

				} else if (status == 501) {

				} else if (status == 503) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendSingleNotification(String tokenId, String title, String message, JSONObject object,ICommunication communication) {
		System.out.println("#INSIDE IN FCMNOTIFICATION");
		List<String> playerIds = new ArrayList<>();
		if (tokenId != null)playerIds.add(tokenId);
		try {
			JSONObject stringJsonBody = new JSONObject();
			stringJsonBody.put("app_id", EnovationConfig.buddyConfig.get(EnovationConstants.FCM_URL_ONE_SIGNAL_APP_ID_NAME));
			stringJsonBody.put("android_group", "myeNovation");
			stringJsonBody.put("android_visibility", "1");
			stringJsonBody.put("content_available", true);
			stringJsonBody.put("android_accent_color", "FF343172");
			JSONObject heading = new JSONObject();
			heading.put("en", title);
			stringJsonBody.put("headings", heading);
			stringJsonBody.put("content_available", true);
			List<String> sug = new ArrayList<>();
			sug.add("All");
			stringJsonBody.put("data", object);
			JSONObject contents = new JSONObject();
			contents.put("en", message);
			stringJsonBody.put("contents", contents);
			stringJsonBody.put("include_player_ids", playerIds);
			System.out.println("Storing New Notification");
			communication.saveNotificationMessage(new NotificationMessage(stringJsonBody.toString(),NotificationConstants.NOTIFICATION_MESSAGE_NT,NotificationConstants.NOTIFICATION_NEW_STATUS));
		}catch(Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
		}	
	}
	
	
	public static void sendSingleNotification1(String tokenId, String title, String message, JSONObject object) {
		System.out.println("#INSIDE IN FCMNOTIFICATION");
		List<String> playerIds = new ArrayList<>();
		if (tokenId != null)playerIds.add(tokenId);
		try {
			String jsonResponse;
			URL url = new URL(EnovationConstants.FCM_URL_ONE_SIGNAL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestMethod("POST");
			JSONObject stringJsonBody = new JSONObject();
			stringJsonBody.put("app_id", EnovationConfig.buddyConfig.get(EnovationConstants.FCM_URL_ONE_SIGNAL_APP_ID_NAME));
			stringJsonBody.put("android_group", "myeNovation");
			stringJsonBody.put("android_visibility", "1");
			stringJsonBody.put("content_available", true);
			stringJsonBody.put("android_accent_color", "FF343172");
			JSONObject heading = new JSONObject();
			heading.put("en", title);
			stringJsonBody.put("headings", heading);
			stringJsonBody.put("content_available", true);
			List<String> sug = new ArrayList<>();
			sug.add("All");
			// stringJsonBody.put("included_segments",sug);
			// JSONObject data = new JSONObject();
			// data.put("sugId", 345);
			stringJsonBody.put("data", object);
			JSONObject contents = new JSONObject();
			contents.put("en", message);
			stringJsonBody.put("contents", contents);
			stringJsonBody.put("include_player_ids", playerIds);
			
			
			System.out.println("#ONE SIGNAL STRING JSON BODY:"+(stringJsonBody.toString()).replace("\"[", "[\"").replace("]\"", "\"]"));
			byte[] sendBytes = (stringJsonBody.toString()).replace("\"[", "[\"").replace("]\"", "\"]")
					.getBytes("UTF-8");
			con.setFixedLengthStreamingMode(sendBytes.length);

			OutputStream outputStream = con.getOutputStream();
			outputStream.write(sendBytes);

			int httpResponse = con.getResponseCode();
			System.out.println("httpResponse: " + httpResponse);
			if (httpResponse >= HttpURLConnection.HTTP_OK && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
				Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			} else {
				Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
				jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
				scanner.close();
			}
			System.out.println("jsonResponse:\n" + jsonResponse);

		} catch (Throwable t) {
			t.printStackTrace();
		}
		/*
		 * try{ URL url = new URL(FCM_URL); HttpURLConnection conn; conn =
		 * (HttpURLConnection) url.openConnection(); conn.setUseCaches(false);
		 * conn.setDoInput(true); conn.setDoOutput(true); conn.setRequestMethod("POST");
		 * conn.setRequestProperty("Authorization","key="+EnovationConfig.buddyConfig.
		 * get("server_key"));
		 * conn.setRequestProperty("Content-Type","application/json"); JSONObject
		 * infoJson = new JSONObject(); infoJson.put("title",title);
		 * infoJson.put("body", message);
		 * infoJson.put("content-available",Boolean.TRUE); infoJson.put("jsonData",
		 * object); JSONObject json = new JSONObject(); json.put("to",tokenId.trim());
		 * json.put("data", infoJson); json.put("content-available",Boolean.TRUE);
		 * object.put("content-available", Boolean.TRUE); //json.put("notification",
		 * object); System.out.println("json :" +json.toString()); OutputStreamWriter wr
		 * = new OutputStreamWriter(conn.getOutputStream()); wr.write(json.toString());
		 * wr.flush(); int status = 0; if( null != conn ){ status =
		 * conn.getResponseCode(); } if( status != 0){
		 * 
		 * if( status == 200 ){ BufferedReader reader = new BufferedReader(new
		 * InputStreamReader(conn.getInputStream()));
		 * System.out.println("Android Notification Response : " + reader.readLine());
		 * }else if(status == 401){
		 * 
		 * System.out.println("Notification Response : TokenId : " + tokenId +
		 * " Error occurred :"); }else if(status == 501){
		 * 
		 * System.out.
		 * println("Notification Response : [ errorCode=ServerError ] TokenId : " +
		 * tokenId); }else if( status == 503){
		 * 
		 * System.out.
		 * println("Notification Response : FCM Service is Unavailable TokenId : " +
		 * tokenId); } } }catch(MalformedURLException mlfexception){
		 * System.out.println("Error occurred while sending push Notification!.." +
		 * mlfexception.getMessage()); }catch(Exception mlfexception){ System.out.
		 * println("Reading URL, Error occurred while sending push Notification!.." +
		 * mlfexception.getMessage()); }
		 */
	}
}
