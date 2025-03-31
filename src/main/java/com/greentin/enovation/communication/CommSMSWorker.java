package com.greentin.enovation.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import org.json.JSONObject;

import com.greentin.enovation.config.EnovationConfig;

public class CommSMSWorker  implements Callable<NotificationProcess>{

	
	public NotificationProcess notificationMessage;
	
	public CommSMSWorker(NotificationProcess notificationMessage) {
	     this.notificationMessage=notificationMessage;
	}
	
	
	public void sendSms(){
		 boolean flag=false;
		try {
			// Construct data
			String apiKey = "apikey=" +EnovationConfig.buddyConfig.get("apikey");
			String message = "&message=" + notificationMessage.getMessageText();
			String sender = "&sender=" + EnovationConfig.buddyConfig.get("senderid");
			String numbers = "&numbers=" + notificationMessage.getContactNumber();
			String unicode = "&unicode=" + 0;
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL(EnovationConfig.buddyConfig.get("smsurl")).openConnection();
			String data = apiKey + numbers + message + sender + unicode;

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			System.out.println("Connecting  api:  "+data);
			System.out.println("Connecting  output:  "+stringBuffer.toString());
			System.out.println("sms sent to :"+notificationMessage.getContactNumber());
			notificationMessage.setConfirmation(stringBuffer.toString());
			JSONObject jsonData =new JSONObject(stringBuffer.toString());
			if(jsonData.has("status") && jsonData.getString("status").equals("success")) {
				flag=true;
			}
		} catch (Exception e) {
			System.out.println("Error SMS "+e);
			notificationMessage.setStatus(NotificationConstants.FAIL);
			notificationMessage.setConfirmation(e.getMessage());
		}
		
		if(flag){
			notificationMessage.setStatus(NotificationConstants.SUCCESS);
		}else if (notificationMessage.getRetryAttempts() < (3-1)) {
			notificationMessage.setStatus(NotificationConstants.NOTIFICATION_RETRY_STATUS);
			notificationMessage.setRetryAttempts(notificationMessage.getRetryAttempts() + 1);
		}else {
			notificationMessage.setStatus(NotificationConstants.FAIL);
		}
	}
		
	@Override
	public NotificationProcess call() throws Exception {
		long threadId = Thread.currentThread().getId();
		System.out.println("**START*** of Thread Id: " + threadId);
		sendSms();
		System.out.println("**END*** of Thread Id: " + threadId);
		return notificationMessage;
	}

}
