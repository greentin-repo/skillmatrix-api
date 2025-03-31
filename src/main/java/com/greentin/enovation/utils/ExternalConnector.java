package com.greentin.enovation.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class ExternalConnector {

	public String getConnector(String apiUrl, String methodType) {

		String output="";
		System.out.println("Connecting  api:  "+apiUrl);
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection httpsCon=(HttpURLConnection) url.openConnection();
			httpsCon.setDoOutput(true);
			httpsCon.setRequestProperty("Method", methodType);		
			System.out.println("Response Code from ext API:  "+httpsCon.getResponseCode());
			BufferedReader br = new BufferedReader(new InputStreamReader((httpsCon.getInputStream())));
			output = br.readLine();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();

		}		

		return output;
	}

}
