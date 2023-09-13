package david.td.taskmanager.controller;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.Task;
import david.td.taskmanager.model.Employee;
import david.td.taskmanager.repository.CompanyRepository;
import david.td.taskmanager.service.ProjectService;
import david.td.taskmanager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/addProject")
    public String addProject(@RequestParam String projectName, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeService.findByUsername(authentication.getName());
        Company company = companyRepository.findByEmployees(employee);

        if (projectService.isProjectNameExistsForCompany(projectName, company)) {
            redirectAttributes.addFlashAttribute("projectExistsError", "This project name already exists for this company.");
        } else {
            Project newProject = new Project();
            newProject.setName(projectName);
            newProject.setCompany(company);
            projectService.addProject(newProject);
        }

        return "redirect:/main";
    }

    @GetMapping("/task-manager/{projectId}")
    public String showTaskManagerPage(@PathVariable Long projectId, Model model) {
        Optional<Project> optionnalProject = projectService.getProjectById(projectId);
        if (!optionnalProject.isPresent()) {
            return "redirect:/main";
        } else {
            Project project = optionnalProject.get();
            List<Task> tasks = project.getTasks();
            List<Employee> employees = employeeService.findAllUsersByCompany(project.getCompany().getId());
            model.addAttribute("project", project);
            model.addAttribute("tasks", tasks);
            model.addAttribute("employees", employees);
        }

        return "task-manager";
    }

    @DeleteMapping("/deleteProject/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        Optional<Project> optionalProject = projectService.getProjectById(projectId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            projectService.deleteProjectAndTasks(project);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Si le projet n'existe pas, retournez une réponse 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/updateProjectName/{projectId}")
    public String updateProjectName(@PathVariable Long projectId, @RequestParam String projectName, RedirectAttributes redirectAttributes) {
        Optional<Project> optionalProject = projectService.getProjectById(projectId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            // Vérifiez si le nouveau nom de projet existe déjà pour cette entreprise
            if (projectService.isProjectNameExistsForCompany(projectName, project.getCompany())) {
                redirectAttributes.addFlashAttribute("projectUpdateError", "The project name already exists.");
            } else {
                project.setName(projectName);
                projectService.saveProject(project);
            }
        }

        return "redirect:/main";
    }

}