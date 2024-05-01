package ro.iss.repository;

import ro.iss.model.Employee;

import java.util.Optional;

public interface EmployeeRepository extends Repository<Long, Employee> {
    Optional<Employee> findByLoginCredentials(String username, String password);
}
