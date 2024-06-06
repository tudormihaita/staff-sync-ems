package ro.iss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.iss.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.project.id = ?1 AND t.status != 'DONE'")
    List<Task> findByProjectId(Long projectId);

    @Query("SELECT t FROM Task t WHERE t.project.manager.id = ?1 AND t.status != 'DONE'")
    List<Task> findByManagerId(Long managerId);

    @Query("SELECT t FROM Task t JOIN t.developers d WHERE d.id = ?1 AND t.status != 'DONE'")
    List<Task> findAssignedTasks(Long developerId);

}
