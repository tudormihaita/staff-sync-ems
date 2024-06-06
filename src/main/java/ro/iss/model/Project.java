package ro.iss.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="projects")
public class Project extends Identifiable<Long> {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @ManyToOne
    @JoinColumn(name="id_manager", nullable = false)
    private ProjectManager manager;

    @Column(name="title")
    private String title;

    @Column(name="client")
    private String client;

    @Column(name="deadline")
    private LocalDate deadline;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name="requirements")
    private String requirements;

    @Column(name="details")
    private String details;

    public Project() {}

    public Project(ProjectManager manager, String title, String client, LocalDate deadline, ProjectStatus status, String requirements, String details) {
        this.manager = manager;
        this.title = title;
        this.client = client;
        this.deadline = deadline;
        this.status = status;
        this.requirements = requirements;
        this.details = details;
    }

    public ProjectManager getManager() {
        return manager;
    }

    public String getClient() {
        return client;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public void setManager(ProjectManager manager) {
        this.manager = manager;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(manager, project.manager) && Objects.equals(client, project.client) && Objects.equals(deadline, project.deadline) && status == project.status && Objects.equals(requirements, project.requirements) && Objects.equals(details, project.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manager, client, deadline, status, requirements, details);
    }

    @Override
    public String toString() {
        return title;
    }
}
