package Spring.Home10.tests.model.task;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_ID")
    private Long id;

    @Column(name = "t_name")
    private String name;

    @Column(name = "t_Description")
    private String description;

    public static Task ofName(String name){
        Task task = new Task();
        task.setName(name);
        return task;
    }

}
