package Spring.Home10.tests.api.services;

import Spring.Home10.tests.model.modelresponse.UserResponse;
import Spring.Home10.tests.model.task.Task;
import Spring.Home10.tests.model.modelresponse.TaskResponse;
import Spring.Home10.tests.model.user.User;
import Spring.Home10.tests.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse createUser(User user) {
        return castUserToUserResponse(user);
    }

    public List<UserResponse> getAll(){
        return repository.findAll()
                .stream()
                .map(this::castUserToUserResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> findById(Long id){
        return repository.findById(id)
                .map(this::castUserToUserResponse);
    }

    public Optional<UserResponse> deleteById(Long id) {
        return repository.findById(id).map(this::castUserToUserResponse);
    }

    private UserResponse castUserToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setPassword(user.getPassword());
        return response;
    }


    public UserResponse save(User user) {
        User savedUser = repository.save(user);
        return castUserToUserResponse(savedUser);
    }
}
