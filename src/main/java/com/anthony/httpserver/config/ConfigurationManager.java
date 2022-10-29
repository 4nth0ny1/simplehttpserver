package com.anthony.httpserver.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.anthony.httpserver.util.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class ConfigurationManager {
	
	// Singleton Pattern - because we dont' need more than one configuration manager 
	
	private static ConfigurationManager myConfigurationManager;
	private static Configuration myCurrentConfiguration;
	
	private ConfigurationManager() {}
	
	public static ConfigurationManager getInstance() {
		if (myConfigurationManager == null) 
			myConfigurationManager = new ConfigurationManager();
		return myConfigurationManager;
	}
	
	/**
	 * used to load a configuration file by the path provided 
	 * @throws IOException 
	 */

	@SuppressWarnings("resource")
	public void loadConfigurationFile(String filePath) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			throw new HttpConfigurationException(e);
		}
		StringBuffer sb = new StringBuffer();
		int i;
		try {
			while((i = fileReader.read()) != -1) {
				sb.append((char)i);
			}
		} catch (IOException e) {
			throw new HttpConfigurationException(e);
		}
		JsonNode conf = null;
		try {
			conf = Json.parse(sb.toString());
		} catch (IOException e) {
			throw new HttpConfigurationException("Error parsing the Configuration File", e);
		}
		try {
			myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
		} catch (JsonProcessingException e) {
			throw new HttpConfigurationException("Error parsing the Configuration file, internal", e);

		}
	}
	
	/**
	 * returns the current loaded configuration 
	 */
	public Configuration getCurrentConfiguration() {
		if (myCurrentConfiguration == null) {
			throw new HttpConfigurationException("No Current Configuration Set"); 
		}
		return myCurrentConfiguration;
	}
}
