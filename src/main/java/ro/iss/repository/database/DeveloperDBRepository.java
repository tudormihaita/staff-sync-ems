package ro.iss.repository.database;

import ro.iss.model.Department;
import ro.iss.model.Developer;
import ro.iss.repository.DeveloperRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DeveloperDBRepository extends AbstractDBRepository<Long, Developer> implements DeveloperRepository {

    public DeveloperDBRepository(Properties properties) {
        super(properties, "developers");
    }

    @Override
    public Developer extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_employee");
        String name = resultSet.getString("name");
        Long badgeNo = resultSet.getLong("badge_no");
        Department department = Department.valueOf(resultSet.getString("department"));
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");

        Timestamp lastLoginTimestamp = resultSet.getTimestamp("last_login");
        LocalDateTime lastLogin = lastLoginTimestamp != null ? lastLoginTimestamp.toLocalDateTime() : null;

        Timestamp lastLogoutTimestamp = resultSet.getTimestamp("last_logout");
        LocalDateTime lastLogout = lastLogoutTimestamp != null ? lastLogoutTimestamp.toLocalDateTime() : null;

        Developer developer =  new Developer(name, badgeNo, department, email, username, password, lastLogin, lastLogout);
        developer.setId(id);
        return developer;
    }

    @Override
    protected PreparedStatement findStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM employees " +
                "INNER JOIN developers ON employees.id_employee = developers.id_developer " +
                "WHERE employees.id_employee = ?");
        findStatement.setLong(1, id);

        return findStatement;
    }

    @Override
    protected PreparedStatement saveStatement(Connection connection, Developer entity) throws SQLException {
        PreparedStatement saveStatement = connection.prepareStatement("INSERT INTO developers (id_developer) VALUES (?)");
        saveStatement.setLong(1, entity.getId());

        return saveStatement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM developers WHERE id_developer = ?");
        deleteStatement.setLong(1, id);

        return deleteStatement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, Developer entity) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE developers SET last_login = ?, last_logout = ? WHERE id_developer = ?");
        Timestamp lastLoginTimestamp = entity.getLastLogin() != null ? Timestamp.valueOf(entity.getLastLogin()) : null;
        Timestamp lastLogoutTimestamp = entity.getLastLogout() != null ? Timestamp.valueOf(entity.getLastLogout()) : null;

        updateStatement.setObject(1, lastLoginTimestamp);
        updateStatement.setObject(2, lastLogoutTimestamp);
        updateStatement.setLong(3, entity.getId());

        return updateStatement;
    }

    @Override
    public List<Developer> findActiveTeamDevelopers(Department department) {
        log.traceEntry("Finding active developers at time {} from department {}", LocalDateTime.now(), department);
        List<Developer> activeDevelopers = new ArrayList<>();

        try (Connection connection = dbUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees " +
                    "INNER JOIN developers ON employees.id_employee = developers.id_developer " +
                    "WHERE employees.department = ? AND last_login IS NOT NULL AND last_logout IS NOT NULL AND (last_login > last_logout)");

            preparedStatement.setString(1, department.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Developer developer = extractEntity(resultSet);
                activeDevelopers.add(developer);
            }

        } catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit(activeDevelopers);
        return activeDevelopers;
    }

    @Override
    public List<Developer> findTeamDevelopers(Department department) {
        log.traceEntry("Finding developers from department {}", department);
        List<Developer> developers = new ArrayList<>();

        try (Connection connection = dbUtils.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM employees " +
                    "INNER JOIN developers ON employees.id_employee = developers.id_developer " +
                    "WHERE employees.department = ?");

            preparedStatement.setString(1, department.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Developer developer = extractEntity(resultSet);
                developers.add(developer);
            }

        } catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit(developers);
        return developers;
    }
}
