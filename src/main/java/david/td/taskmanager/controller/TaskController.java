package david.td.taskmanager.controller;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import david.td.taskmanager.model.Employee;
import david.td.taskmanager.repository.ProjectRepository;
import david.td.taskmanager.repository.TaskRepository;
import david.td.taskmanager.service.TaskService;
import david.td.taskmanager.service.UserService;
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
    private UserService userService;

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

    @PostMapping("/deleteTask/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus("deleted");
            taskRepository.save(task);

            return ResponseEntity.ok("Task deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

    @PostMapping("/editTask/{taskId}")
    public String editTask(@PathVariable Long taskId, @RequestParam String newTaskName, @RequestParam Long selectedEmployee) {
        Task task = taskService.getTaskById(taskId);
        if (task != null) {
            task.setName(newTaskName);
            Employee employee = userService.getUserById(selectedEmployee);
            if (employee != null) {
                task.setAssignedEmployee(employee);
            }
            taskService.saveTask(task);
        }
        return "redirect:/task-manager/" + task.getProject().getId();
    }


}
