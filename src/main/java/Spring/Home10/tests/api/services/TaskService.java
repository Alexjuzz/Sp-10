package Spring.Home10.tests.api.services;
import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.modelresponse.TaskResponse;
import Spring.Home10.tests.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;


    private TaskResponse castTaskToTaskResponse(Task task){
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        return  response;
    }


    public List<TaskResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(this::castTaskToTaskResponse)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponse> findById(Long id){
        return repository.findById(id)
                .map(this::castTaskToTaskResponse);
    }

    public TaskResponse save(Task task) {
        Task savedTask = repository.save(task);
        return castTaskToTaskResponse(savedTask);
    }

    public Optional<TaskResponse> deleteById(Long id) {
       return repository.findById(id).map(this::castTaskToTaskResponse);
    }
}
