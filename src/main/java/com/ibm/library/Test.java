package com.ibm.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.ibm.library.model.AppProperties;

@Component
public class Test implements CommandLineRunner {
	@Autowired
	AppProperties appProperties;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("###########"+appProperties.getMessage());
		
	}
}
