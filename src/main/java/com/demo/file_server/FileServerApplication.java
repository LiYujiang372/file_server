package com.demo.file_server;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.demo.file_server.server.TcpServer;

@SpringBootApplication
public class FileServerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(FileServerApplication.class, args);
		try {
			context.getBean(TcpServer.class).start();
		} catch (BeansException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
