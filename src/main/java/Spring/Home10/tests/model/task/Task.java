package Spring.Home10.tests.model.task;

import Spring.Home10.tests.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "t_name")
    private String name;

    @Column(name = "t_Description")
    private String description;
    @Column(name = "Date_Start", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @Column(name = "Status_at_now")
    private Status status;


    @OneToMany(mappedBy = "task", cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonManagedReference
    private List<User> userList = new ArrayList<>();

    public static Task ofName(String name){
        Task task = new Task();
        task.setName(name);
        return task;
    }
    @PrePersist
    private void onCreate() {
        date = LocalDate.now();
        status = Status.NOT_STARTED;
    }
}
