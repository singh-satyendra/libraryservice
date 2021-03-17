package com.ibm.library.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
//@PropertySource("classpath:application.properties")
//Last file (myapplication.properties) has higher priority than first (myapplication2.properties). But if application.properties is in classpath then application.properties will have the highest priority
//@PropertySource(value = {"file:/config/myapplication2.properties", "file:/config/myapplication.properties"})
public class AppProperties {
		
	String name;
	String message;
	Mail mail;
	
	public static class Mail {
		String host;
		int port;
		String protocol;
		
		public String getHost() {
			return host;
		}
		public void setHost(String host) {
			this.host = host;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getProtocol() {
			return protocol;
		}
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
