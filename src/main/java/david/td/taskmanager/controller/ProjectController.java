package david.td.taskmanager.controller;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Project;
import david.td.taskmanager.model.User;
import david.td.taskmanager.repository.CompanyRepository;
import david.td.taskmanager.service.ProjectService;
import david.td.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/addProject")
    public String addProject(@RequestParam String projectName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        Company company = companyRepository.findByEmployees(user);
        Project newProject = new Project();
        newProject.setName(projectName);
        newProject.setCompany(company);
        projectService.addProject(newProject);

        return "redirect:/main"; // Redirect back to the main page after adding the project
    }

    @GetMapping("/task-manager/{projectId}")
    public String showTaskManagerPage(@PathVariable Long projectId, Model model) {
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);

        return "task-manager";
    }
}