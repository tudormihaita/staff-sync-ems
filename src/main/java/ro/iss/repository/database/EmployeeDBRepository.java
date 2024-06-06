package ro.iss.repository.database;

import ro.iss.model.Department;
import ro.iss.model.Developer;
import ro.iss.model.Employee;
import ro.iss.repository.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

public class EmployeeDBRepository extends AbstractDBRepository<Long, Employee> {

    public EmployeeDBRepository(Properties properties) {
        super(properties, "employees");
    }

    @Override
    public Employee extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_employee");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        Long badgeNo = resultSet.getLong("badge_no");
        Department department = Department.valueOf(resultSet.getString("department"));
        String email = resultSet.getString("email");


        Employee employee = new Employee(name, badgeNo, department, email, username, password);
        employee.setId(id);
        return employee;
    }

    @Override
    protected PreparedStatement findStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM employees WHERE id_employee = ?");
        findStatement.setLong(1, id);

        return findStatement;
    }

    @Override
    protected PreparedStatement saveStatement(Connection connection, Employee entity) throws SQLException {
        PreparedStatement saveStatement = connection.prepareStatement("INSERT INTO employees(name, badge_no, department, email, username, password) " +
                "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        saveStatement.setString(1, entity.getName());
        saveStatement.setLong(2, entity.getBadgeNo());
        saveStatement.setString(3, entity.getDepartment().toString());
        saveStatement.setString(4, entity.getEmail());
        saveStatement.setString(5, entity.getUsername());
        saveStatement.setString(6, entity.getPassword());

        return saveStatement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM employees WHERE id_employee = ?");
        deleteStatement.setLong(1, id);

        return deleteStatement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, Employee entity) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE employees SET name = ?, badge_no = ?, department = ?, email = ?, username = ?, password = ? " +
                "WHERE id_employee = ?");
        updateStatement.setString(1, entity.getName());
        updateStatement.setLong(2, entity.getBadgeNo());
        updateStatement.setString(3, entity.getDepartment().toString());
        updateStatement.setString(4, entity.getEmail());
        updateStatement.setString(5, entity.getUsername());
        updateStatement.setString(6, entity.getPassword());
        updateStatement.setLong(7, entity.getId());

        return updateStatement;
    }

    public Optional<Employee> findByLoginCredentials(String username, String password) {
        log.traceEntry("Finding employee with given login credentials {}", username);

        try(Connection connection = dbUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees " +
                    "WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Employee employee = extractEntity(resultSet);

                log.traceExit(employee);
                return Optional.of(employee);
            }

        } catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit("Employee not found!");
        return Optional.empty();
    }
}
