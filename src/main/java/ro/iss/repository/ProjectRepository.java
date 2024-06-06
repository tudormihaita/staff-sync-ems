package ro.iss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.iss.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
    @Query("SELECT p FROM Project p WHERE p.manager.id = ?1")
    List<Project> findAllByManagerId(Long managerId);

    @Query("SELECT p FROM Project p WHERE p.manager.id = ?1 AND p.status != 'DEPLOYED'")
    List<Project> findActiveByManagerId(Long projectManagerId);
}
