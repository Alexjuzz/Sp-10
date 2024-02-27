package Spring.Home10.tests.api.services;

import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.user.User;
import Spring.Home10.tests.repositories.TaskRepository;
import Spring.Home10.tests.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Этот класс содержит модульные тесты для класса TaskService.
 */
@ActiveProfiles("test") // Указание активного профиля "test" для тестирования
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Конфигурация Spring Boot контекста
@AutoConfigureWebTestClient // Автоконфигурация WebTestClient для тестирования
class TaskServiceTest {
    @Autowired
    WebTestClient webTestClient; // Веб-клиент для тестирования

    @Autowired
    TaskRepository taskRepository; // Репозиторий задач
    @Autowired
    UserRepository userRepository; // Репозиторий пользователей

    /**
     * Перечисление для представления различных состояний задачи.
     */
    public static enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    /**
     * Класс ответа для объектов User, специфичный для JUnit-тестирования.
     */
    @Data // Аннотация Lombok для генерации геттеров и сеттеров
    static class JUnitUserResponse {
        private Long id;
        private String name;
        private String password;
        private Task task;
    }

    /**
     * Класс ответа для объектов Task, специфичный для JUnit-тестирования.
     */
    @Data // Аннотация Lombok для генерации геттеров и сеттеров
    static class JUnitTaskResponse {
        private Long id;
        private String name;
        private String description;
        private LocalDate date;
        private Status status;
        private List<User> userList;
    }

    /**
     * Метод, выполняемый перед каждым тестовым случаем.
     * Он очищает репозиторий задач.
     */
    @BeforeEach
    void setUp() {
        taskRepository.deleteAll(); // Очистка репозитория задач перед каждым тестом
    }

    /**
     * Тест для проверки поиска задачи по ID, когда она не существует.
     */
    @Test
    void testFindByNotFound() {
        // Отправляем GET-запрос для поиска задачи по ID, который не существует
        webTestClient.get()
                .uri("/api/task/-1")
                .exchange()
                .expectStatus().isNotFound(); // Проверяем, что возвращается статус "Not Found"
    }

    /**
     * Тест для проверки поиска задачи по ID, когда она существует.
     */
    @Test
    void testFindByIdSuccess() {
        // Создание и сохранение задачи в репозитории
        Task task = taskRepository.save(Task.ofName("test_task"));
        // Отправка GET-запроса для поиска задачи по ID
        JUnitTaskResponse response = webTestClient.get()
                .uri("/api/task/" + task.getId())
                .exchange()
                .expectStatus().isOk() // Ожидаем успешный статус ответа
                .expectBody(JUnitTaskResponse.class) // Ожидаем тело ответа в формате JUnitTaskResponse
                .returnResult() // Получаем результат выполнения запроса
                .getResponseBody(); // Получаем тело ответа
        // Проверка ответа
        Assertions.assertNotNull(response); // Проверяем, что ответ не является пустым
        Assertions.assertEquals(task.getId(), response.getId()); // Проверяем соответствие ID задачи в ответе
        Assertions.assertEquals(task.getName(), response.getName()); // Проверяем соответствие имени задачи в ответе
    }

    /**
     * Тест для проверки получения всех задач.
     */
    @Test
    void testGetAllTask() {
        // Сохранение нескольких задач в репозитории
        taskRepository.saveAll(List.of(
                Task.ofName("task1"),
                Task.ofName("task999"),
                Task.ofName("x")
        ));
        // Получение всех задач из репозитория
        List<Task> tasks = taskRepository.findAll();
        // Отправка GET-запроса для получения всех задач
        List<JUnitTaskResponse> responses = webTestClient.get()
                .uri("/api/task/")
                .exchange()
                .expectStatus().isOk() // Ожидаем успешный статус ответа
                .expectBody(new ParameterizedTypeReference<List<JUnitTaskResponse>>() {
                }) // Ожидаем список задач в формате JUnitTaskResponse
                .returnResult() // Получаем результат выполнения запроса
                .getResponseBody(); // Получаем тело ответа
        // Проверка ответа
        Assertions.assertEquals(tasks.size(), responses.size()); // Проверяем, что количество задач соответствует ожидаемому
        // Проверяем, что все задачи из репозитория присутствуют в ответе
        for (JUnitTaskResponse response : responses) {
            boolean found = tasks.stream()
                    .filter(it -> Objects.equals(it.getId(), response.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), response.getName()));
            Assertions.assertTrue(found); // Проверяем, что задача найдена в ответе
        }
    }

    /**
     * Тест для проверки удаления задачи.
     */
    @Test
    void testDeleteTask() {
        // Сохранение нескольких задач в репозитории
        List<Task> repository = taskRepository.saveAll(List.of(
                Task.ofName("task1"),
                Task.ofName("task999"),
                Task.ofName("x")
        ));
        // Получение ID задачи для удаления
        Long taskIdToDelete = repository.get(0).getId();
        // Отправка DELETE-запроса для удаления задачи
        webTestClient.delete()
                .uri("/api/task/del/" + taskIdToDelete)
                .exchange()
                .expectStatus().isOk(); // Ожидаем успешный статус ответа
        // Проверка удаления задачи
        List<Task> afterDeleteList = taskRepository.findAll();
        Assertions.assertEquals(repository.size() - 1, afterDeleteList.size()); // Проверяем, что задача удалена из репозитория
        Assertions.assertTrue(taskRepository.findAll().stream().noneMatch(task -> task.getId().equals(taskIdToDelete))); // Проверяем, что задача удалена успешно
    }

    /**
     * Тест для проверки сохранения новой задачи.
     */
    @Test
    void testSaveTask() {
        int initialSize = taskRepository.findAll().size(); // Получаем начальный размер списка задач
        // Отправка POST-запроса для создания новой задачи
        webTestClient.post()
                .uri("/api/task/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Task.ofName("task"))
                .exchange()
                .expectStatus().isCreated(); // Ожидаем успешный статус ответа
        // Проверяем, что размер списка задач увеличился на 1 после создания новой задачи
        Assertions.assertEquals(initialSize + 1, taskRepository.findAll().size());
    }

    /**
     * Тест для проверки назначения пользователя на задачу.
     */
    @Test
    void testPutUserInTask() {
        // Создание нового пользователя и отправка POST-запроса для его создания
        JUnitUserResponse userResponse = webTestClient.post()
                .uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(User.ofName("user312"))
                .exchange()
                .expectStatus().isCreated() // Ожидаем успешный статус ответа
                .expectBody(JUnitUserResponse.class) // Ожидаем тело ответа в формате JUnitUserResponse
                .returnResult() // Получаем результат выполнения запроса
                .getResponseBody(); // Получаем тело ответа

        Assertions.assertNotNull(userResponse); // Проверяем, что ответ не является пустым
        // Создание новой задачи и отправка POST-запроса для назначения пользователю этой задачи
        Task task = taskRepository.save(Task.ofName("task1"));
        webTestClient.post()
                .uri("/api/task/put/"+task.getId()+"/user_id/"+userResponse.getId())
                .exchange()
                .expectStatus().isAccepted(); // Ожидаем успешный статус ответа
        Assertions.assertNotNull(task.getUserList()); // Проверяем, что список пользователей задачи не является пустым
    }

}
