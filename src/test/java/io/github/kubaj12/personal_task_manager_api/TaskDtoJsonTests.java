package io.github.kubaj12.personal_task_manager_api;

import io.github.kubaj12.personal_task_manager_api.dto.TaskDto;

import io.github.kubaj12.personal_task_manager_api.entity.TaskPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.OffsetDateTime;

@JsonTest
public class TaskDtoJsonTests {

    @Autowired
    private JacksonTester<TaskDto> json;

    @Autowired
    private JacksonTester<TaskDto[]> jsonList;

    private TaskDto[] taskArray;

    @BeforeEach
    void setUp() {
        taskArray = new TaskDto[]{
                TaskDto.builder()
                        .id(1L)
                        .user_id(1L)
                        .title("Aktualizacja dokumentacji")
//                        .description("Użytkownicy zgłaszają błąd 500 przy próbie resetu hasła.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.MEDIUM)
                        .deadline(OffsetDateTime.parse("2026-01-10T12:00:00+02:00"))
                        .build(),

                // 2. Zadanie już po terminie (wczoraj)
                TaskDto.builder()
                        .id(2L)
                        .title("Aktualizacja dokumentacji")
                        .description("Opisać nowe endpointy w Swaggerze.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.MEDIUM)
                        .deadline(OffsetDateTime.now().minusDays(1))
                        .build(),

                // 3. Zadanie wykonane (z konkretnym offsetem UTC)
                TaskDto.builder()
                        .id(3L)
                        .title("Konfiguracja CI/CD")
                        .description("Ustawienie pipeline'u na GitHub Actions.")
                        .status(TaskStatus.DONE)
                        .priority(TaskPriority.HIGH)
                        .deadline(OffsetDateTime.parse("2026-01-10T12:00:00+02:00"))
                        .build(),

                // 4. Zadanie o niskim priorytecie z odległym terminem
                TaskDto.builder()
                        .id(4L)
                        .title("Refactoring modułu raportów")
                        .description("Poprawa czytelności kodu w klasach GeneratorService.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.LOW)
                        .deadline(OffsetDateTime.now().plusMonths(1))
                        .build(),

                // 5. Zadanie pilne (na dzisiejszy wieczór)
                TaskDto.builder()
                        .id(5L)
                        .title("Spotkanie z klientem")
                        .description("Prezentacja makiety funkcjonalności profilu użytkownika.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.HIGH)
                        .deadline(OffsetDateTime.now().withHour(18).withMinute(0))
                        .build()
        };
    }

    @Test
    void taskDtoSerializationTest() throws IOException {
        TaskDto task = taskArray[0];

        assertThat(json.write(task)).isStrictlyEqualToJson("singleTaskDto.json");

        assertThat(json.write(task)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(task)).extractingJsonPathNumberValue("@.id").isEqualTo(1);

        assertThat(json.write(task)).hasJsonPathNumberValue("@.user_id");
        assertThat(json.write(task)).extractingJsonPathNumberValue("@.user_id").isEqualTo(1);

        assertThat(json.write(task)).hasJsonPathStringValue("@.title");
        assertThat(json.write(task)).extractingJsonPathStringValue("@.title").isEqualTo("Aktualizacja dokumentacji");

        assertThat(json.write(task)).hasEmptyJsonPathValue("@.description");

        assertThat(json.write(task)).hasJsonPathStringValue("@.priority");
        assertThat(json.write(task)).extractingJsonPathStringValue("@.priority").isEqualTo(TaskPriority.MEDIUM.name());

        assertThat(json.write(task)).hasJsonPathStringValue("@.status");
        assertThat(json.write(task)).extractingJsonPathStringValue("@.status").isEqualTo(TaskStatus.TODO.name());

        assertThat(json.write(task)).hasJsonPathStringValue("@.deadline");
        assertThat(json.write(task)).extractingJsonPathStringValue("@.deadline").isEqualTo(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss+02:00").format(OffsetDateTime.parse("2026-01-10T12:00:00+02:00")));
    }

    @Test
    void taskDtoDeserializationTest() throws IOException {
        TaskDto task = taskArray[0];
        String taskString = """
                {
                  "id": 1,
                  "user_id": 1,
                  "title": "Aktualizacja dokumentacji",
                  "description": null,
                  "priority": "MEDIUM",
                  "status": "TODO",
                  "deadline": "2026-01-10T12:00:00+02:00"
                }
                """;
        assertThat(json.parse(taskString)).usingRecursiveComparison().ignoringFields("deadline").isEqualTo(task);
        assertThat(json.parseObject(taskString).deadline().toInstant()).isEqualTo(task.deadline().toInstant());

        assertThat(json.parseObject(taskString).id()).isEqualTo(1);
        assertThat(json.parseObject(taskString).user_id()).isEqualTo(1);
        assertThat(json.parseObject(taskString).title()).isEqualTo("Aktualizacja dokumentacji");
        assertThat(json.parseObject(taskString).description()).isEqualTo(null);
        assertThat(json.parseObject(taskString).priority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(json.parseObject(taskString).status()).isEqualTo(TaskStatus.TODO);
        assertThat(json.parseObject(taskString).deadline().toInstant()).isEqualTo(OffsetDateTime.parse("2026-01-10T12:00:00+02:00").toInstant());
    }
}
