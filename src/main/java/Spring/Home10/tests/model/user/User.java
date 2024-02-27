package Spring.Home10.tests.model.user;


import Spring.Home10.tests.model.role.Role;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Username")
    private String name;

    @Column(name = "User_password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
