package com.greentin.enovation.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.communication.NotificationConstants;
import com.greentin.enovation.communication.NotificationMessage;
import com.greentin.enovation.config.EnovationConfig;

public class SmsUtil implements Runnable {

	private String number;
	private String content;

	private ICommunication communication;

	public SmsUtil(String number, String content, ICommunication communication) {
		this.number = number;
		this.content = content;
		this.communication = communication;
	}

	public void sendSms() {
		System.out.println("Storing Message in Notification=" + number);
		try {
			NotificationMessage nm = new NotificationMessage(number, content,
					NotificationConstants.NOTIFICATION_MESSAGE_SMS, NotificationConstants.NOTIFICATION_NEW_STATUS);
			communication.saveNotificationMessage(nm);
		} catch (Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
		}

	}

	public void sendSms1() {
		/*
		 * String smsUrl=EnovationConfig.buddyConfig.get("smsurl");
		 * 
		 * smsUrl=smsUrl.replaceAll(Pattern.quote("{mobileno}"),number);
		 * smsUrl=smsUrl.replaceAll(Pattern.quote("{senderid}"),EnovationConfig.
		 * buddyConfig.get("senderid"));
		 * content=content.replaceAll(Pattern.quote(" "),"+");
		 * smsUrl=smsUrl.replaceAll(Pattern.quote("{msg}"),content);
		 * System.out.println(smsUrl); String output="";
		 * System.out.println("Connecting  api:  "+smsUrl);
		 * System.out.println("Connecting  output:  "+output); try { URL url = new
		 * URL(smsUrl); HttpURLConnection httpsCon=(HttpURLConnection)
		 * url.openConnection(); httpsCon.setDoOutput(true);
		 * httpsCon.setRequestProperty("Method", smsUrl);
		 * System.out.println("Response Code from ext API:  "+httpsCon.getResponseCode()
		 * ); BufferedReader br = new BufferedReader(new
		 * InputStreamReader((httpsCon.getInputStream()))); output = br.readLine();
		 * br.close(); System.out.println("sms sent to :"+number); } catch (Exception e)
		 * { System.out.println("Exception occured while sending sms to :"+number);
		 * e.printStackTrace(); }
		 */

		// return output;
		// String output=externalConnector.getConnector(smsUrl, "GET");
		// System.out.println(output);
		try {
			// Construct data
			String apiKey = "apikey=" + EnovationConfig.buddyConfig.get("apikey");
			String message = "&message=" + content;
			String sender = "&sender=" + EnovationConfig.buddyConfig.get("senderid");
			String numbers = "&numbers=" + number;
			String unicode = "&unicode=" + 0;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL(EnovationConfig.buddyConfig.get("smsurl"))
					.openConnection();
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
			System.out.println("Connecting  api:  " + data);
			System.out.println("Connecting  output:  " + stringBuffer.toString());
			System.out.println("sms sent to :" + number);
		} catch (Exception e) {
			System.out.println("Error SMS " + e);
		}
		// String output=externalConnector.getConnector(smsUrl, "GET");
		// System.out.println(output);
	}

	@Override
	public void run() {
		sendSms();

	}

}
