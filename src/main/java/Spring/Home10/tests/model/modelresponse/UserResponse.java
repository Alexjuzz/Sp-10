package Spring.Home10.tests.model.modelresponse;

import Spring.Home10.tests.model.task.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String password;

}
