package Spring.Home10.tests.model.modelresponse;

import Spring.Home10.tests.model.task.Status;
import Spring.Home10.tests.model.user.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate date;
    private Status status;
    private List<User> userList;
}
