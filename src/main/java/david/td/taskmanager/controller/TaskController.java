package david.td.taskmanager.controller;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import david.td.taskmanager.repository.ProjectRepository;
import david.td.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;
    @PostMapping("/addTask/{projectId}")
    public String addTask(@PathVariable Long projectId, @RequestParam String taskName) {
        Task task = new Task();
        task.setName(taskName);

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            task.setProject(project);
            task.setStatus("todo");
            project.getTasks().add(task);

            projectRepository.save(project);
            taskRepository.save(task);
        }
        return "redirect:/task-manager/" + projectId;
    }

}
