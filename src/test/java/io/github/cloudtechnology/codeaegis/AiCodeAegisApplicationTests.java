package io.github.cloudtechnology.codeaegis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ut")
@Import(TestAiCodeAegisApplication.class)
@Testcontainers(disabledWithoutDocker = true)
class AiCodeAegisApplicationTests {

	// @Test
	// void contextLoads() {
	// }

}
