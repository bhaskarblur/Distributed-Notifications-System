package com.bhaskarblur.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class UserApplication {

	private static int port;

	private static final Logger logger = LoggerFactory.getLogger(UserApplication.class);

	public UserApplication(@Value("${server.port:3001}") int port) {
		UserApplication.port = port;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(UserApplication.class)
				.properties("server.port=" + port)
				.run(args);

		logger.info("✨ Started User service on port: {}", port);
	}

}
