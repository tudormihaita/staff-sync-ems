package ro.iss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="project_managers")
public class ProjectManager extends Employee {
    @Column(name="client_satisfaction")
    private Double clientSatisfactionRating;

    public ProjectManager(String name, Long badgeNo, Department department, String email, String username, String password) {
        super(name, badgeNo, department, email, username, password);
    }
    public ProjectManager() {
        super();
    }

    public ProjectManager(String name, Long badgeNo, Department department, String email, String username, String password, Double clientSatisfactionRating) {
        super(name, badgeNo, department, email, username, password);
        this.clientSatisfactionRating = clientSatisfactionRating;
    }

    public ProjectManager(Double clientSatisfactionRating) {
        this.clientSatisfactionRating = clientSatisfactionRating;
    }

    public Double getClientSatisfactionRating() {
        return clientSatisfactionRating;
    }

    public void setClientSatisfactionRating(Double clientSatisfactionRating) {
        this.clientSatisfactionRating = clientSatisfactionRating;
    }

    @Override
    public String toString() {
        return "ProjectManager{" +
                "clientSatisfactionRating=" + clientSatisfactionRating +
                '}';
    }
}
