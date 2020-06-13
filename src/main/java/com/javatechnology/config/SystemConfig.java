package com.javatechnology.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component 
public class SystemConfig {
	@Value("${example.property}")
	private String exampleProperty;
	@Value("${redis.server}")
	private String redisServer;
	@Value("${redis.port}")
	private int redisport;
	
	public String getProperty() {
		
		return exampleProperty;
	}
	
	public String getRedisServer() {
		return redisServer;
	}
	public int getRedisPort() {
		return redisport;
	}

}
