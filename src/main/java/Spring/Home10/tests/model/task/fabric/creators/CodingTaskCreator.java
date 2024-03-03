package Spring.Home10.tests.model.task.fabric.creators;

import Spring.Home10.tests.model.task.fabric.CodingTask;
import Spring.Home10.tests.model.task.fabric.interfaces.iTask;

public class CodingTaskCreator extends TaskCreator {
    @Override
    public iTask createTask() {
        return new CodingTask();
    }
}
