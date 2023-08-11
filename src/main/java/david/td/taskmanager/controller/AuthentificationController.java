package david.td.taskmanager.controller;

import david.td.taskmanager.model.User;
import david.td.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthentificationController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
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
    public String processRegisterForm(@ModelAttribute("user") User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String showMainPage() {
        return "main";
    }
}
