package com.cba.core.wiremeweb;

import com.cba.core.wiremeweb.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class AuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}

}
