package david.td.taskmanager.service;

import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import david.td.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public boolean updateTaskStatus(Long taskId, String newStatus) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setStatus(newStatus);
            taskRepository.save(task);
            return true;
        }

        return false;
    }

    public Task getTaskById(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.orElse(null);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public void deleteTasksByProject(Project project) {
        if (project != null) {
            // Supprimer toutes les tâches liées au projet
            List<Task> tasks = taskRepository.findByProject(project);
            taskRepository.deleteAll(tasks);
        }
    }
}