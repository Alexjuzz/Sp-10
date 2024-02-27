package Spring.Home10.tests.api.taskcontroller;

import Spring.Home10.tests.api.services.TaskService;
import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.modelresponse.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @GetMapping("/")
    public List<TaskResponse> getAllCustomers() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@RequestBody Task task){
        return new ResponseEntity<>(service.save(task),HttpStatus.CREATED);
    }

    @PostMapping("/put/{task_id}/user_id/{user_id}")
    public ResponseEntity<TaskResponse> putUser(@PathVariable("task_id") Long task_id,@PathVariable() Long user_id){
        return new ResponseEntity<>(service.addUserToTask(task_id,user_id),HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/del/{task_id}/user_id/{user_id}")
    public ResponseEntity<TaskResponse> delUser(@PathVariable("task_id") Long task_id,@PathVariable() Long user_id){
        return new ResponseEntity<>(service.deleteUserFromTusk(task_id,user_id),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<TaskResponse> deleteById(@PathVariable Long id){
        return service.deleteById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }

}
