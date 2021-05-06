package com.example.artapp.service.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationProperties("storage")
@ComponentScan
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "upload";
	private String loadLocation = "results";
	private String modelLocation = "models";
	private String logLocation = "logs/log.txt";

	public String getLogLocation() {
		return logLocation;
	}

	public String getLocation() {
		return location;
	}
	public String getLoadLocation() {
		return loadLocation;
	}
	public String getModelLocation() {
		return modelLocation;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
