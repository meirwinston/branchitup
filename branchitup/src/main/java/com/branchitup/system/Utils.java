/*
 * YBLOB PROPRIETARY/CONFIDENTIAL.
 * 
 * yBlob Proprietary - USE PURSUANT TO COMPANY INSTRUCTIONS
 * USE of this information by anyone and for any purpose may only be 
 * made by the prior written consent of yBlob.  This 
 * confidential information is owned by yBlob, and is 
 * protected under United States copyright laws and international treaties.
 */
package com.branchitup.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class Utils {
	private static Properties branchitupProperties;
	private static File docroot = null;
	
	static{
		try {
			String filePath = Constants.BRANCHITUP_ROOTDIR + "/config/" + Constants.PROPS_FILE_NAME;
			File file = new File(filePath);
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				branchitupProperties = new Properties();
				branchitupProperties.load(fis);
			}
			else{
				throw new YBlobSystemException(Constants.PROPS_FILE_NAME + " file does not exist in " + Constants.BRANCHITUP_ROOTDIR + "/config/");
			}
		} 
		catch (Exception exp) {
			Constants.LOGGER.error(exp);
		}
	}
	
	public static Set<Entry<Object, Object>> getProperties(){
		return branchitupProperties.entrySet();
	}
	
	public static void setProperty(String key,String value){
		branchitupProperties.setProperty(key, value);
	}

	public static String getProperty(String key){
		return branchitupProperties.getProperty(key);
	}
	
	public static String getProperty(String key,String defaultValue){
		return branchitupProperties.getProperty(key,defaultValue);
	}
	
	public static String stringifyMap(Map<String,Object> map){
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String,Object>> itr = map.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String, Object> e = itr.next();
			sb.append(e.getKey());
			sb.append(":");
			sb.append(e.getValue());
			if(itr.hasNext()){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public static Integer getIntProperty(String key,Integer defaultValue){
		Integer val =  defaultValue;
		try {
			String prop = branchitupProperties.getProperty(key);
			if(prop != null){
				val = Integer.valueOf(prop);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return val;
	}
	
	public static String domainName(){
		return System.getProperty("com.sun.aas.domainName");
	}
	
	public static String getDomainRoot(){
		String name = System.getProperty("com.sun.jbi.domain.root");
		if(name == null){
			name = (domainsRoot() + "/" + domainName());
		}
		return name;
	}
	
	/*
	 * NOTE: WKExporter is using the call File.createTempFile
	 * we need to set JVM property rather then just branchitup property
	 */
	public static String getTmpDir(){
		return System.getProperty("java.io.tmpdir");
//		return branchitupProperties.getProperty("branchitup.tmp.dir");
	}
	
	public static String domainsRoot(){
		return System.getProperty("com.sun.aas.domainsRoot");
	}
	
	public static String installRoot(){
		return System.getProperty("com.sun.jbi.platform.installRoot");
	}
	
	public static String instanceRoot(){
		return System.getProperty("com.sun.aas.instanceRoot");
	}
	
	public static String instanceRootURI(){
		return System.getProperty("com.sun.aas.instanceRootURI");
	}
	
	public static String hostName(){
		return System.getProperty("com.sun.aas.hostName");
	}
	
	public static String javaRoot(){
		return System.getProperty("com.sun.aas.javaRoot");
	}
	
	public static File getDocrootDirectory() {
		if (docroot == null) {
			docroot = new File(getDomainRoot() + "/docroot");
			try {
				docroot = docroot.getCanonicalFile();
			} 
			catch (Exception exp) {
				docroot = docroot.getAbsoluteFile();
			}
		}
		return docroot;
	}
	
	/**
	 * Environment dependent code, for debugging purposes only
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getGlassfishProperty(String propertyName,String user,String passwordFile){ //*.*.*.*
		String prop = null;
		String command = installRoot() + "/bin/asadmin get --user " + user + " --passwordfile " + passwordFile + " " + propertyName;
		try {
			Process process = Runtime.getRuntime().exec(command);
			prop = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	static{
		System.out.println(System.getProperties());
	}
//	public void main(String[] args){
//		System.out.println(Utils.getTmpDir());
//	}

	
}
