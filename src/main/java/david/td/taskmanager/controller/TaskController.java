package david.td.taskmanager.controller;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import david.td.taskmanager.repository.ProjectRepository;
import david.td.taskmanager.repository.TaskRepository;
import david.td.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TaskController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskService taskService;

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

    @PostMapping("/updateTaskStatus/{taskId}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status) {
        boolean success = taskService.updateTaskStatus(taskId, status);

        if (success) {
            return ResponseEntity.ok("Statut de la tâche mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de la mise à jour du statut de la tâche.");
        }
    }

}
