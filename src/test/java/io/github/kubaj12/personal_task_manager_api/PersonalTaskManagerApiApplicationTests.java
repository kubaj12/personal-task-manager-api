package io.github.kubaj12.personal_task_manager_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonalTaskManagerApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		assertThat(1).isEqualTo(1);
	}

	@Test
	@DirtiesContext
	void ShouldCreateANewTask() {

	}
}
