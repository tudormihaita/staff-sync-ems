package ro.iss.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.iss.model.Identifiable;
import ro.iss.repository.CrudRepository;
import ro.iss.repository.utils.JDBCUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public abstract class AbstractDBRepository<ID extends Serializable, E extends Identifiable<ID>> implements CrudRepository<ID, E> {
    protected final String table;
    protected final JDBCUtils dbUtils;
    protected static final Logger log = LogManager.getLogger(AbstractDBRepository.class);

    protected AbstractDBRepository(Properties properties, String table) {
        log.info("Initializing {} with properties: {}", this.getClass().getSimpleName(), properties);
        this.table = table;
        dbUtils = new JDBCUtils(properties);
    }


    public abstract E extractEntity(ResultSet resultSet) throws SQLException;
    protected abstract PreparedStatement findStatement(Connection connection, ID id) throws SQLException;
    protected abstract PreparedStatement saveStatement(Connection connection, E entity) throws SQLException;
    protected abstract PreparedStatement deleteStatement(Connection connection, ID id) throws SQLException;
    protected abstract PreparedStatement updateStatement(Connection connection, E entity) throws SQLException;


    @Override
    public Optional<E> findOne(ID id) throws IllegalArgumentException {
        log.traceEntry("Finding entity with id {} in " + table, id);
        Connection connection = dbUtils.getConnection();

        try(PreparedStatement findStatement = findStatement(connection, id)) {
            ResultSet resultSet = findStatement.executeQuery();
            if(resultSet.next()) {
                E entity = extractEntity(resultSet);
                log.traceExit(entity);
                return Optional.ofNullable(entity);
            }
        }
        catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit("No entity found in " + table + "with id {}", id);
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        log.traceEntry("Retrieving all entities from " + table);
        Connection connection = dbUtils.getConnection();
        List<E> entities = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + table)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                E entity = extractEntity(resultSet);
                entities.add(entity);
            }
        }
        catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit(entities);
        return entities;
    }

    @Override
    public Optional<E> save(E entity) throws IllegalArgumentException {
        log.traceEntry("Saving entity {} in " + table, entity);
        Connection connection = dbUtils.getConnection();

        try(PreparedStatement saveStatement = saveStatement(connection, entity)) {
            int affectedRows = saveStatement.executeUpdate();
            if(affectedRows == 1) {
                ResultSet generatedKeys = saveStatement.getGeneratedKeys();
                if(generatedKeys.next()) {
                    Object generatedId = generatedKeys.getObject(1);
                    entity.setId((ID) generatedId);
                }

                log.traceExit("Entity {} saved successfully", entity);
                return Optional.of(entity);
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            throw new RuntimeException(sqlException);
        }

        log.traceExit("No rows affected, save was unsuccessful");
        return Optional.empty();
    }

    @Override
    public Optional<E> delete(ID id) throws IllegalArgumentException {
        log.traceEntry("Deleting entity with id {} from " + table, id);

        Optional<E> optionalEntity = findOne(id);
        if(optionalEntity.isPresent()) {
            Connection connection = dbUtils.getConnection();
            try (PreparedStatement deleteStatement = deleteStatement(connection, id)) {
                int affectedRows = deleteStatement.executeUpdate();
                if (affectedRows == 1) {
                    log.traceExit("Entity {} deleted successfully", optionalEntity.get());
                    return optionalEntity;
                }
            } catch (SQLException sqlException) {
                log.error(sqlException);
                throw new RuntimeException(sqlException);
            }
        }

        log.traceExit("Entity with given id doesn't exist, delete unsuccessful");
        return Optional.empty();
    }

    @Override
    public Optional<E> update(E entity) throws IllegalArgumentException {
        log.traceEntry("Updating entity with id {}  as {} from " + table, entity.getId(), entity);
        Optional<E> optionalEntity = findOne(entity.getId());

        if(optionalEntity.isPresent()) {
            Connection connection = dbUtils.getConnection();
            try(PreparedStatement updateStatement = updateStatement(connection, entity)) {
                int affectedRows = updateStatement.executeUpdate();
                if (affectedRows == 1) {
                    log.traceExit("Entity {} updated successfully", optionalEntity.get());
                    return optionalEntity;
                }
            } catch (SQLException sqlException) {
                log.error(sqlException);
                throw new RuntimeException(sqlException);
            }
        }

        log.traceExit("Entity with given id doesn't exist, update unsuccessful");
        return Optional.empty();
    }
}
