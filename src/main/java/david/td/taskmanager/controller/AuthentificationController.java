package david.td.taskmanager.controller;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Employee;
import david.td.taskmanager.model.Project;
import david.td.taskmanager.repository.CompanyRepository;
import david.td.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthentificationController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        List<Company> companies = companyRepository.findAll();
        model.addAttribute("companies", companies);
        return "register";
    }

    @PostMapping("/login")
    public String processLoginForm(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        if (userService.authenticateUser(username,password)){
            return "redirect:/main";
        } else{
            redirectAttributes.addFlashAttribute("error", "Nom d'utilisateur incorrect");
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String processRegisterForm(@ModelAttribute("user") Employee employee, @RequestParam("companyId") Long company) {
        userService.registerUser(employee, company);
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String showMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = userService.findByUsername(authentication.getName());
        Company company = companyRepository.findByEmployees(employee);
        List<Project> projects = company.getProjects();
        model.addAttribute("company", company);
        model.addAttribute("employee", employee);
        model.addAttribute("projects", projects);
        return "main";
    }
}
