package ro.iss.repository.database;

import ro.iss.model.Department;
import ro.iss.model.ProjectManager;
import ro.iss.repository.ProjectManagerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class ProjectManagerDBRepository extends AbstractDBRepository<Long, ProjectManager> implements ProjectManagerRepository {
    public ProjectManagerDBRepository(Properties properties) {
        super(properties, "project_managers");
    }

    @Override
    public ProjectManager extractEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id_employee");
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String name = resultSet.getString("name");
        Long badgeNo = resultSet.getLong("badge_no");
        Department department = Department.valueOf(resultSet.getString("department"));
        Double clientSatisfaction = resultSet.getDouble("client_satisfaction");

        ProjectManager projectManager = new ProjectManager(name, badgeNo, department, email, username, password, clientSatisfaction);
        projectManager.setId(id);
        return projectManager;
    }

    @Override
    protected PreparedStatement findStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM employees " +
                "INNER JOIN project_managers ON employees.id_employee = project_managers.id_manager " +
                "WHERE employees.id_employee = ?");
        findStatement.setLong(1, id);

        return findStatement;
    }

    @Override
    protected PreparedStatement saveStatement(Connection connection, ProjectManager entity) throws SQLException {
        PreparedStatement saveStatement = connection.prepareStatement("INSERT INTO project_managers (id_manager, client_satisfaction) VALUES (?, ?)");
        saveStatement.setLong(1, entity.getId());
        saveStatement.setDouble(2, entity.getClientSatisfactionRating());

        return saveStatement;
    }

    @Override
    protected PreparedStatement deleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM project_managers WHERE id_manager = ?");
        deleteStatement.setLong(1, id);

        return deleteStatement;
    }

    @Override
    protected PreparedStatement updateStatement(Connection connection, ProjectManager entity) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE project_managers SET client_satisfaction = ? WHERE id_manager = ?");
        updateStatement.setDouble(1, entity.getClientSatisfactionRating());

        return updateStatement;
    }
}
