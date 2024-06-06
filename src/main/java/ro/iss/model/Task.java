package ro.iss.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="tasks")
public class Task extends Identifiable<Long> {
    @ManyToOne
    @JoinColumn(name="id_project")
    private Project project;

    @Column(name="title")
    private String title;

    @Column(name="deadline")
    private LocalDate deadline;

    @Column(name="priority")
    @Enumerated(EnumType.STRING)
    private PriorityLevel priority;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name="details")
    private String details;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Developer> developers = new ArrayList<>();

    public Task() {}

    public Task(Project project, String title, LocalDate deadline, PriorityLevel priority, TaskStatus status, String details) {
        this.project = project;
        this.title = title;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.details = details;
        this.developers = new ArrayList<>();
    }

    public Project getProject() {
        return project;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDevelopers(List<Developer> assignedDevelopers) {
        this.developers = assignedDevelopers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(project, task.project) && Objects.equals(title, task.title) && Objects.equals(deadline, task.deadline) && priority == task.priority && status == task.status && Objects.equals(details, task.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, title, deadline, priority, status, details);
    }

    @Override
    public String toString() {
        return title;
    }
}
