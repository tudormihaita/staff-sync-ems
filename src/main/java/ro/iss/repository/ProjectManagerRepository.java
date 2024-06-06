package ro.iss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.iss.model.ProjectManager;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager, Long> {
}
