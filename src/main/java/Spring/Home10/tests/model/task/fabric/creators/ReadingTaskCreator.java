package Spring.Home10.tests.model.task.fabric.creators;

import Spring.Home10.tests.model.task.fabric.ReadingTask;
import Spring.Home10.tests.model.task.fabric.interfaces.iTask;

public class ReadingTaskCreator extends TaskCreator{


    @Override
    public iTask createTask() {
        return new ReadingTask();
    }
}
