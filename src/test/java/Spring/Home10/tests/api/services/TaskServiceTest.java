package Spring.Home10.tests.api.services;

import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.user.User;
import Spring.Home10.tests.repositories.TaskRepository;
import Spring.Home10.tests.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TaskServiceTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    public static enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLITED
    }

    @Data
    static class JUnitUserResponse {
        private Long id;
        private String name;
        private String password;
        private Task task;
    }


    @Data
    static class JUnitTaskResponse {
        private Long id;
        private String name;
        private String description;
        private LocalDate date;
        private Status status;
        private List<User> userList;
    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testFindByNotFound() {
        webTestClient.get()
                .uri("/api/task/-1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testFindByIdSuccess() {
        Task task = taskRepository.save(Task.ofName("test_task"));
        JUnitTaskResponse response = webTestClient.get()
                .uri("/api/task/" + task.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitTaskResponse.class)
                .returnResult()
                .getResponseBody();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(task.getId(), response.getId());
        Assertions.assertEquals(task.getName(), response.getName());
    }

    @Test
    void testGetAllTusk() {
        taskRepository.saveAll(List.of(
                Task.ofName("task1"),
                Task.ofName("task999"),
                Task.ofName("x")
        ));
        List<Task> tasks = taskRepository.findAll();
        List<JUnitTaskResponse> responses = webTestClient.get()
                .uri("/api/task/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitTaskResponse>>() {
                })
                .returnResult()
                .getResponseBody();
        Assertions.assertEquals(tasks.size(), responses.size());
        for (JUnitTaskResponse response : responses) {
            boolean found = tasks.stream()
                    .filter(it -> Objects.equals(it.getId(), response.getId()))
                    .anyMatch(it -> Objects.equals(it.getName(), response.getName()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void testDelTusk() {
        List<Task> repository = taskRepository.saveAll(List.of(
                Task.ofName("task1"),
                Task.ofName("task999"),
                Task.ofName("x")
        ));
        Long taskIdToDel = repository.get(0).getId();
        webTestClient.delete()
                .uri("/api/task/del/" + taskIdToDel)
                .exchange()
                .expectStatus().isOk();

        List<Task> afterdelList = taskRepository.findAll();
        Assertions.assertEquals(repository.size() - 1, afterdelList.size());
        Assertions.assertTrue(taskRepository.findAll().stream().noneMatch(task -> task.getId().equals(taskIdToDel)));
    }

    @Test
    void testSaveTask() {
        int a = taskRepository.findAll().size();
        webTestClient.post()
                .uri("/api/task/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Task.ofName("task"))
                .exchange()
                .expectStatus().isCreated();
        Assertions.assertEquals(a + 1, (Integer) taskRepository.findAll().size());
    }

    @Test
    void testPutUserInTask() {
        JUnitUserResponse userResponse = webTestClient.post()
                .uri("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(User.ofName("user312"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitUserResponse.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(userResponse);
        Task task = taskRepository.save(Task.ofName("task1"));
        webTestClient.post()
                .uri("/api/task/put/"+task.getId()+"/user_id/"+userResponse.getId())
                .exchange()
                .expectStatus().isAccepted();
        Assertions.assertNotNull(task.getUserList());

    }

}