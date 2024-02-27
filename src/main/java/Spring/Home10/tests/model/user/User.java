package Spring.Home10.tests.model.user;

import Spring.Home10.tests.model.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "Username")
    private String name;

    @Column(name = "User_password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "id")
    @JsonBackReference
    private Task task;

    public static User ofName(String name) {
        User u  = new User();
        u.setName(name);
        return u;
    }
}