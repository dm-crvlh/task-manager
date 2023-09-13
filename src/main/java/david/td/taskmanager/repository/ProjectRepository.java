package david.td.taskmanager.repository;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findById(Long id);

    Project findByCompanyAndName(Company company, String projectName);
}
