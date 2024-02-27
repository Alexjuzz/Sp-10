package Spring.Home10.tests.repositories;

import Spring.Home10.tests.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {
}
