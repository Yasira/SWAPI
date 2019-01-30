package com.sw.api.swapi;


import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import com.sun.jersey.api.client.WebResource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SwApiTest {
	
	private static final Logger log = Logger.getLogger(SwApiTest.class);
	WebResource webResource;
	
	String configPath=System.getProperty("user.dir")+"\\configdata\\config.properties";
	Client client;
	
	@BeforeMethod(groups = { "commonTest"})
	public void setUp(Method method) throws Exception {
		
		client = Client.create();
		Properties log4jProperties=new Properties();
		log4jProperties.load(new FileInputStream(System.getProperty("user.dir")+"\\configdata\\log4j.properties"));
		//log4jProperties.setProperty("log4j.appender.file.File",System.getProperty("user.dir")+"Logs\\"+method.getName()+".log");
	    PropertyConfigurator.configure(log4jProperties);
	
		
		
		
		
	}
	/**
	 * Test Case: To check the response of the API
	 * GET all  http://swapi.co/api/planets
	 *  Verify: Receive 200 Kk Response and ensure valid json payload shows up
	 * @throws Exception
	 */
	
	@Test(groups = { "APITest"})
	public void swPlanetsAllTest()  throws Exception{
		String URL=Api_ClientUtil.readPropertiesFile(configPath,"URL");
		
		webResource = client .resource(URL);
		
		ClientResponse response = webResource.accept("application/json").header("user-agent", " ")
                .get(ClientResponse.class);
		
		assertTrue(response.getStatus() == 200,"Response status is not 200"+response.getStatus());
		String output = response.getEntity(String.class);
		log.info("Output response json "+output);
		assertTrue(Api_ClientUtil.isJSONValid(output),"Response is not a valid json format");
		assertTrue(output.contains("count"),"Response is not a valid json format");
		
		
	}

	/**
	 * Verify Get by ID Request
	 * http://swapi.co/api/planets/{id}
	 * 
	 * Verify: Specified details shows up with ID 
	 * Proper json payload is displayed
	 * Verify the name associated with ID
	 * 
	 * @throws Exception
	 */
	@Test(groups = { "APITest"})
	public void swPlanetsWithId() throws Exception {
		String URL=Api_ClientUtil.readPropertiesFile(configPath,"URL");
		String path=Api_ClientUtil.readPropertiesFile(configPath,"PathID");
		
		webResource = client .resource(URL);
		
		ClientResponse response = webResource.path(path).accept("application/json").header("user-agent", " ")
                .get(ClientResponse.class);
		
		assertTrue(response.getStatus() == 200,"Response status is not 200, response code is:"+response.getStatus());
		
		String output = response.getEntity(String.class);
		log.info("Output response json "+output);
		assertTrue(Api_ClientUtil.isJSONValid(output),"Response is not a valid json format");
		assertTrue(output.contains("Alderaan"),"Output response is not as expected");
		
		
		
	}
}
