package david.td.taskmanager.service;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public void addProject(Project project) {
        projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

}
