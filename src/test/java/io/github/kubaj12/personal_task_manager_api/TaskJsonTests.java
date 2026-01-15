package io.github.kubaj12.personal_task_manager_api;
import io.github.kubaj12.personal_task_manager_api.entity.Task;

// dodac zaleznosci spring boot do testowania JSOna
// Ten obiekt i adnotacje

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class TaskJsonTests {

    @Autowired
    private JacksonTester<Task> json;

    @Autowired
    private JacksonTester<Task[]> jsonList;

    private Task[] taskList;

    @BeforeEach
    void setUp() {
//        tasksList = Arrays.array(new Tasks(),
//                new Tasks(),
//                new Tasks(),
//                new Tasks()
//                )
    }

    @Test
    void taskSerializationTest() {
//        Tasks tasks = new Tasks();
    }
}
