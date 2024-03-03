package Spring.Home10.tests.model.task.fabric.creators;

import Spring.Home10.tests.model.task.fabric.interfaces.iTask;

public  abstract  class TaskCreator {
    public abstract iTask createTask();

    public void performTask(){
        iTask task = createTask();
    }
}
