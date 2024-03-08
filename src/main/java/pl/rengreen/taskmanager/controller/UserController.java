package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
public class UserController {

    private UserService userService;
    private CompanyService companyService;
    @Autowired
    public UserController(UserService userService,CompanyService companyService) {
        this.userService = userService;
        this.companyService=companyService;
    }

    @GetMapping("/users")
    public String listUsers(Principal principal,Model model, SecurityContextHolderAwareRequestWrapper request) {
        boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");
        String email=principal.getName();
        User user=userService.getUserByEmail(email);
        List<User> allUsers=companyService.getCompanyUsers(user.getCompany().getId());
        model.addAttribute("users", allUsers);
        model.addAttribute("isAdminSigned", isAdminSigned);
        return "views/users";
    }

    @GetMapping("user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

}
