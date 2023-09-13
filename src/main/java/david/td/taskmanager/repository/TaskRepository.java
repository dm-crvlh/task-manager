package david.td.taskmanager.repository;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);

    boolean existsByNameAndProject(String taskName, Project project);
}
