package io.github.kubaj12.personal_task_manager_api;

import io.github.kubaj12.personal_task_manager_api.dto.TaskDto;
import io.github.kubaj12.personal_task_manager_api.entity.Task;

// dodac zaleznosci spring boot do testowania JSOna
// Ten obiekt i adnotacje

import io.github.kubaj12.personal_task_manager_api.entity.TaskPriority;
import io.github.kubaj12.personal_task_manager_api.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.OffsetDateTime;

@JsonTest
public class TaskJsonTests {

    @Autowired
    private JacksonTester<Task> json;

    @Autowired
    private JacksonTester<Task[]> jsonList;

    private TaskDto[] taskArray;

    @BeforeEach
    void setUp() {
        taskArray = new TaskDto[]{
                TaskDto.builder()
                        .id(1L)
                        .title("Naprawa błędu w logowaniu")
//                        .description("Użytkownicy zgłaszają błąd 500 przy próbie resetu hasła.")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(TaskPriority.HIGH)
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
    void taskSerializationTest() {
        TaskDto task = taskArray[0];

        // do obiektu json
        // sprawdzic czy json zgadza sie z wartosicami task
    }
}
