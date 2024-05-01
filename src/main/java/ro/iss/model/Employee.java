package ro.iss.model;

public class Employee extends Entity<Long> {
    private final String name;
    private final Long badgeNo;
    private final Department department;
    private final String email;
    private final String username;
    private final String password;

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
