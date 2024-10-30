package com.hoanhtuan.main_app_security;

import com.hoanhtuan.main_app_security.utils.Handle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class MainAppSecurityApplication {

	public static void main(String[] args) {
		Handle.addRequestId(Handle.generateRequestIdStart());
		SpringApplication.run(MainAppSecurityApplication.class, args);
		LOG.info("Security application started");
	}

}