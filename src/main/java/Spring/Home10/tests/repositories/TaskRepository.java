package Spring.Home10.tests.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import Spring.Home10.tests.model.task.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
