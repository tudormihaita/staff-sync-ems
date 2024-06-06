package ro.iss.model;

import jakarta.persistence.*;

@Entity
@Table(name="employees")
@Inheritance(strategy = InheritanceType.JOINED)
public class Employee extends Identifiable<Long> {
    @Column(name="name")
    private String name;

    @Column(name="badge_no")
    private Long badgeNo;

    @Column(name="department")
    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;
    public Employee() {}

    public Employee(String name, Long badgeNo, Department department, String email, String username, String password) {
        this.name = name;
        this.badgeNo = badgeNo;
        this.department = department;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Long getBadgeNo() {
        return badgeNo;
    }

    public Department getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBadgeNo(Long badgeNo) {
        this.badgeNo = badgeNo;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", badgeNo=" + badgeNo +
                ", department=" + department +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
