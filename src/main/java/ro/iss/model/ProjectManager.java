package ro.iss.model;

public class ProjectManager extends Employee {
    private Double clientSatisfactionRating;
    public ProjectManager(String name, Long badgeNo, Department department, String email, String username, String password) {
        super(name, badgeNo, department, email, username, password);
    }

    public ProjectManager(String name, Long badgeNo, Department department, String email, String username, String password, Double clientSatisfactionRating) {
        super(name, badgeNo, department, email, username, password);
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
