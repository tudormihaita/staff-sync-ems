package ro.iss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.iss.model.Department;
import ro.iss.model.Developer;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    @Query("SELECT d FROM Developer d WHERE d.department = ?1 AND d.lastLogin IS NOT NULL AND d.lastLogout IS NOT NULL " +
                  "and d.lastLogin > d.lastLogout")
    List<Developer> findActiveTeamDevelopers(Department department);

    @Query("SELECT d FROM Developer d WHERE d.department = ?1")
    List<Developer> findTeamDevelopers(Department department);
}
