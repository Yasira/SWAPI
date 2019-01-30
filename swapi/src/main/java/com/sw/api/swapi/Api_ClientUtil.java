package com.sw.api.swapi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Api_ClientUtil{
	
	private static final Logger log = Logger.getLogger(Api_ClientUtil.class);
	
	public static String readPropertiesFile(String file_name, String key) throws IOException{
		System.out.println("file name is "+file_name);
		
		try{
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream(file_name);
		prop.load(fs);
		String value = prop.get(key).toString();
		
		log.info("value is "+value);
		return value;
		}
		catch(FileNotFoundException e){
			
			return null;
		}
 }
	//CHecking for duplicate key , valid json
	public static boolean isJSONValid(String test) {
	    try {
	        isValidJSONStructure(test);
	        JsonFactory factory = new JsonFactory();
	        JsonParser parser = factory.createJsonParser(test);
	        while (!parser.isClosed()) {
	            parser.nextToken();
	        }
	    } catch (Exception e) {
	        log.error("exception: ", e);
	        return false;
	    }
	    return true;
	}

	//Checking for Json structure valid with no open paranethesis etc..
	private static void isValidJSONStructure(String test) throws Exception {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            log.error("exception: ", ex);
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            log.error("exception: ", ex1);
	            throw new Exception("Invalid JSON.");
	        }
	    }
	}
}