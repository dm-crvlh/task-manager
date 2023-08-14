package david.td.taskmanager.service;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository; // Assuming you have a ProjectRepository

    public void addProject(Project project) {
        projectRepository.save(project);
    }
}
