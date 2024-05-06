package com.canattofilipe.springseckeycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringSecKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringSecKeycloakApplication::main).with(TestSpringSecKeycloakApplication.class).run(args);
	}

}
