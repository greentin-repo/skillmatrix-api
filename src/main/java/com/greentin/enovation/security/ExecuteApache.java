package com.greentin.enovation.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.greentin.enovation.config.EnovationConfig;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 *Created by rkashid on 21/11/18.
 */
@Component
public class ExecuteApache {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteApache.class);

	@Value("${app.apache.user}")
	private String userName;

	@Value("${app.apache.pass}")
	private String password;


	@Value("${app.apache.host}")
	private String host;

	@Value("${app.apche.port}")
	private int port;

	public  int executeFile(String scriptFileName){
		List<String> result = new ArrayList<String>();
		int exitStatus = 0;
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(userName, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
			InputStream in = channelExec.getInputStream();
			channelExec.setCommand(scriptFileName);
			channelExec.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null)
			{
				result.add(line);
			}
			exitStatus = channelExec.getExitStatus();
			channelExec.disconnect();
			session.disconnect();

			if(exitStatus < 0){
				System.out.println("Done, but exit status not set!");
			}
			else if(exitStatus > 0){
				System.out.println("Done, but with error!");
			}
			else{
				System.out.println("Done!");
			}
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e);
		}
		return exitStatus;
	}

	public  boolean WriteConfigFileOnServer(String serverDomainName) {
		boolean flag=true;
		try {
				JSch jsch = new JSch();
				Session session = null;
				session = jsch.getSession(userName, host, port);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				    session.connect();
				ChannelSftp channel = null;
				channel = (ChannelSftp)session.openChannel("sftp");
		        channel.connect();
		        
		        ChannelSftp sftpChannel = (ChannelSftp) channel;
		        sftpChannel.cd(EnovationConfig.buddyConfig.get("masterConfigFileWritePath"));
		        
				String fileContent=(EnovationConfig.buddyConfig.get("masterConfigTemp")).replace("{serverdomain}", serverDomainName);
				System.out.println(fileContent);
				File file=new File(EnovationConfig.buddyConfig.get("masterConfigFileWritePath"));
				File dire =new File(EnovationConfig.buddyConfig.get("masterConfigFileWritePath")+serverDomainName+".conf");
				if (!file.exists()){
					file.mkdirs();
					if (dire.createNewFile()){
						System.out.println("File is created!");
					} else {
						System.out.println("File already exists.");
					}
				}
	
				FileWriter stream = new FileWriter(dire);
				stream.write(fileContent.toString());
				stream.close();
				
				sftpChannel.put(new FileInputStream(dire), EnovationConfig.buddyConfig.get("masterConfigFileWritePath")+serverDomainName+".conf"); 
			    sftpChannel.exit();
			    dire.delete();
				channel.disconnect();
				session.disconnect();
			} catch (JSchException e) {
				e.printStackTrace();
				flag=false;
			}catch(Exception e) {
				e.printStackTrace();
				flag=false;
			}finally {
				
				logger.info("INSIDE IN FINALLY BLOOCK(WriteConfigFileOnServer)");
			}
			

		return flag;
	}
	
	public boolean Writefile() {
		String filename="setup6.digitizhr.com";
		try {
			JSch jsch = new JSch();
			Session session = null;
			session = jsch.getSession(userName, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			    session.connect();
			ChannelSftp channel = null;
			channel = (ChannelSftp)session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftpChannel = (ChannelSftp) channel;
	        sftpChannel.cd(EnovationConfig.buddyConfig.get("masterConfigFileWritePath"));
	        
	        String fileContent=(EnovationConfig.buddyConfig.get("masterConfigTemp")).replace("{serverdomain}", filename);
			System.out.println(fileContent);
			File file=new File("/Users/rakesh/Desktop/");
			File dire =new File("/Users/rakesh/Desktop/"+filename+".conf");
			if (!file.exists()){
				file.mkdirs();
				if (dire.createNewFile()){
					System.out.println("File is created!");
				} else {
					System.out.println("File already exists.");
				}
			}

			FileWriter stream = new FileWriter(dire);
			stream.write(fileContent.toString());
			stream.close();
			
	        sftpChannel.put(new FileInputStream(dire), EnovationConfig.buddyConfig.get("masterConfigFileWritePath")+filename+".conf"); // here you can also change the target file name by replacing f.getName() with your choice of name
	        sftpChannel.exit();
	        dire.delete();
	        channel.disconnect();
			session.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	

}
