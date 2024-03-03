package Spring.Home10.tests.model.task.fabric;

import Spring.Home10.tests.model.task.Task;

public interface TaskFactory {
    Task createTask(String type);
}
