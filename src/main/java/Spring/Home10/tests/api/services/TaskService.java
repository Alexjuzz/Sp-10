package Spring.Home10.tests.api.services;

import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.modelresponse.TaskResponse;

import Spring.Home10.tests.model.task.fabric.TaskFactoryService;
import Spring.Home10.tests.model.user.User;
import Spring.Home10.tests.repositories.TaskRepository;
import Spring.Home10.tests.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TaskService {
    //    @Autowired
    private final TaskFactoryService factoryService;

    @Autowired
    private TaskRepository repository;
    @Autowired
    private UserRepository userRepository;

    public TaskService(TaskFactoryService factoryService, TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.factoryService = factoryService;
        this.userRepository = userRepository;
    }

    private TaskResponse castTaskToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        response.setDate(task.getDate());
        response.setStatus(task.getStatus());
        response.setUserList(task.getUserList());
        return response;
    }


    public List<TaskResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::castTaskToTaskResponse)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponse> findById(Long id) {
        return repository.findById(id)
                .map(this::castTaskToTaskResponse);
    }
    @Transactional
    public TaskResponse save(String task_type, Task task) {
        Task saved = factoryService.createTask(task_type);

        if (saved == null) {
            throw new RuntimeException("task type not found");
        }

        saved.setId(task.getId());
        saved.setStatus(task.getStatus());
        saved.setDate(task.getDate());
        saved.setUserList(task.getUserList());
        saved.setDescription(task.getDescription());
        saved.setName(task.getName());



        Task savedTask = repository.save(saved);
        System.out.println(savedTask);
        return castTaskToTaskResponse(savedTask);
    }

    public Optional<TaskResponse> deleteById(Long id) {
        Optional<TaskResponse> taskResponse = repository.findById(id).map(this::castTaskToTaskResponse);
        repository.deleteById(taskResponse.get().getId());
        return taskResponse;
    }

    public TaskResponse addUserToTask(Long task_id, Long user_id) {
        Task task = repository.findById(task_id).orElseThrow(() -> new RuntimeException("Not found task"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setTask(task);
        task.getUserList().add(user);
        repository.save(task);
        return castTaskToTaskResponse(task);
    }

    public TaskResponse deleteUserFromTusk(Long task_id, Long user_id) {
        Task task = repository.findById(task_id).orElseThrow(() -> new RuntimeException("Not found task"));
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setTask(null);
        task.getUserList().remove(user);
        repository.save(task);
        return castTaskToTaskResponse(task);
    }


}
