package Spring.Home10.tests.model.task.fabric;

import Spring.Home10.tests.model.task.Task;
import org.springframework.stereotype.Service;


@Service
public class TaskFactoryService implements TaskFactory{

    public Task createTask(String type) {

        switch (type) {
            case "urgent":
                return new UrgentTask();
            case "regular":
                return new RegularTask();
            case "other":
                return Task.ofName("Other task");
            default:
                throw  new RuntimeException("Not found task type");
        }
    }

}
