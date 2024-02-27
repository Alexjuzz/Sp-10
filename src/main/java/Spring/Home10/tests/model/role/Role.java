package Spring.Home10.tests.model.role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Set;

@Entity
@Table(name = "t_role")
public class Role  {

    @Id
    private Long id;
    private String name;

}
