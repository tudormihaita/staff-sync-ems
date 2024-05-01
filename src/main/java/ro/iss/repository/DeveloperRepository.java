package ro.iss.repository;

import ro.iss.model.Department;
import ro.iss.model.Developer;

import java.time.LocalDateTime;
import java.util.List;

public interface DeveloperRepository extends Repository<Long, Developer> {
    List<Developer> findActiveTeamDevelopers(Department department);

    List<Developer> findTeamDevelopers(Department department);
}
