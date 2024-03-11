package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class SuperAdminController {

    private UserService userService;
    private CompanyService companyService;

    @Autowired
    public SuperAdminController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/superAdmin/assignManager")
    public String assignManager(Principal principal, Model model, SecurityContextHolderAwareRequestWrapper request) {
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> allUsers = companyService.getCompanyUsers(user.getCompany().getId());
        model.addAttribute("users", allUsers);
        model.addAttribute("isAdminSigned", isAdminSigned);

        return "views/assignManager";
    }

    @GetMapping("/superAdmin/makeManager/{id}")
    public String makeManager(@PathVariable Long id) {
        User user = userService.getUserById(id);
        userService.changeRoleToAdmin(user);
        return "redirect:/superAdmin/assignManager";
    }

    @GetMapping("/superAdmin/removeManager/{id}")
    public String removeManager(@PathVariable Long id) {
        User user = userService.getUserById(id);
        userService.changeRoleToUser(user);
        return "redirect:/superAdmin/assignManager";
    }

}
