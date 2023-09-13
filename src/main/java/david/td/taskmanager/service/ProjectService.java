package david.td.taskmanager.service;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Project;
import david.td.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskService taskService;

    public void addProject(Project project) {
        projectRepository.save(project);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public void deleteProjectAndTasks(Project project) {
        if (project != null) {
            // Supprimer d'abord toutes les tâches liées au projet
            taskService.deleteTasksByProject(project);

            // Ensuite, supprimer le projet lui-même
            projectRepository.delete(project);
        } else {
            throw new IllegalArgumentException("Project cannot be null");
        }
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public boolean isProjectNameExistsForCompany(String projectName, Company company) {
        Project existingProject = projectRepository.findByCompanyAndName(company, projectName);

        return existingProject != null;
    }
}
