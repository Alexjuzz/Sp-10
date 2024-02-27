package Spring.Home10.tests.api.taskcontroller;

import Spring.Home10.tests.api.services.UserService;
import Spring.Home10.tests.model.modelresponse.TaskResponse;
import Spring.Home10.tests.model.modelresponse.UserResponse;
import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private  final  UserService service;
    @GetMapping("/")
    public List<UserResponse> getAllCustomers() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createTask(@RequestBody User user){
        return new ResponseEntity<>(service.save(user),HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<UserResponse> deleteById(@PathVariable Long id){
        return service.deleteById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build());
    }
}
