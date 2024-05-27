package io.github.cloudtechnology.codeaegis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class AiCodeAegisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiCodeAegisApplication.class, args);
	}

}
