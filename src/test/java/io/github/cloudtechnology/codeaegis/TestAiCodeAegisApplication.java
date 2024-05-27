package io.github.cloudtechnology.codeaegis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestAiCodeAegisApplication {

	public static void main(String[] args) {
		SpringApplication.from(AiCodeAegisApplication::main).with(TestAiCodeAegisApplication.class).run(args);
	}

}
