package ro.iss.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="developers")
public class Developer extends Employee {
    @Nullable
    private LocalDateTime lastLogin;
    @Nullable
    private LocalDateTime lastLogout;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="assigned_tasks",
            joinColumns = @JoinColumn(name="id_developer"),
            inverseJoinColumns = @JoinColumn(name="id_task")
    )
    private List<Task> tasks = new ArrayList<>();

    public Developer() {}

    public Developer(String name, Long badgeNo, Department department, String email, String username, String password, LocalDateTime lastLogin, LocalDateTime lastLogout) {
        super(name, badgeNo, department, email, username, password);
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
        this.tasks = new ArrayList<>();
    }

    public Developer(String name, Long badgeNo, Department department, String email, String username, String password) {
        super(name, badgeNo, department, email, username, password);
    }

    @Nullable
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    @Nullable
    public LocalDateTime getLastLogout() {
        return lastLogout;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setLastLogin(@Nullable LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLastLogout(@Nullable LocalDateTime lastLogout) {
        this.lastLogout = lastLogout;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "lastLogin=" + lastLogin +
                ", lastLogout=" + lastLogout +
                '}';
    }
}
