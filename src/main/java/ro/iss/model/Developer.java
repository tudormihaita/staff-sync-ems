package ro.iss.model;

import java.time.LocalDateTime;

public class Developer extends Employee {
    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;

    public Developer(String name, Long badgeNo, Department department, String email, String username, String password, LocalDateTime lastLogin, LocalDateTime lastLogout) {
        super(name, badgeNo, department, email, username, password);
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
    }

    public Developer(String name, Long badgeNo, Department department, String email, String username, String password) {
        super(name, badgeNo, department, email, username, password);
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public LocalDateTime getLastLogout() {
        return lastLogout;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLastLogout(LocalDateTime lastLogout) {
        this.lastLogout = lastLogout;
    }

    @Override
    public String toString() {
        return "Developer{" +
                "lastLogin=" + lastLogin +
                ", lastLogout=" + lastLogout +
                '}';
    }
}
