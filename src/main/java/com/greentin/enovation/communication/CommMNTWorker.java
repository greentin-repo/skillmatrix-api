package com.greentin.enovation.communication;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.greentin.enovation.utils.EnovationConstants;

public class CommMNTWorker implements Callable<NotificationProcess> {

    public NotificationProcess notificationMessage;
	
	public CommMNTWorker(NotificationProcess notificationMessage) {
	     this.notificationMessage=notificationMessage;
	}
	
	public  void sendSingleNotification() {
		boolean flag=false;
		System.out.println("#INSIDE IN FCMNOTIFICATION");
		try {
			String jsonResponse;
			URL url = new URL(EnovationConstants.FCM_URL_ONE_SIGNAL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestMethod("POST");
			
			System.out.println("#ONE SIGNAL STRING JSON BODY:"+(notificationMessage.getMessageText().toString()).replace("\"[", "[\"").replace("]\"", "\"]"));
			byte[] sendBytes = (notificationMessage.getMessageText()).replace("\"[", "[\"").replace("]\"", "\"]")
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
			System.out.println("jsonResponse:" + jsonResponse);
			notificationMessage.setConfirmation(jsonResponse);
			
			JSONObject data =new JSONObject(jsonResponse);
			if(data.has("recipients") && data.getInt("recipients")>0) {
				flag=true;
			}
		}catch(Throwable t) {
			System.out.println(ExceptionUtils.getStackTrace(t));
			System.out.println(t.getMessage());
			notificationMessage.setConfirmation(t.getMessage());
		}
		
		if(flag){
			notificationMessage.setStatus(NotificationConstants.SUCCESS);
		}else if(notificationMessage.getRetryAttempts() < (3-1)) {
			notificationMessage.setStatus(NotificationConstants.NOTIFICATION_RETRY_STATUS);
			notificationMessage.setRetryAttempts(notificationMessage.getRetryAttempts() + 1);
		}else{
			notificationMessage.setStatus(NotificationConstants.FAIL);
		}
	}
	
	@Override
	public NotificationProcess call() throws Exception {
		long threadId = Thread.currentThread().getId();
		System.out.println("**START*** of Thread Id: " + threadId);
		
		sendSingleNotification();
		
		System.out.println("**END*** of Thread Id: " + threadId);
		return notificationMessage;
	}

}
