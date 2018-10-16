package com.demo.file_server.context;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppConfigs {
	
	public static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
	
}
