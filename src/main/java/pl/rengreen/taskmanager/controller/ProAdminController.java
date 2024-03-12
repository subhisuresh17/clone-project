package pl.rengreen.taskmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cloudinary.http44.api.Response;

import pl.rengreen.taskmanager.model.Role;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
@RequestMapping("/proAdmin")
public class ProAdminController {
    private UserService userService;
    private CompanyService companyService;

    public ProAdminController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/createSuperAdmin/{companyId}")
    public String createSuperAdmin(@PathVariable("companyId") long companyId, Model model) {
        model.addAttribute("company", companyId);
        return "forms/createSuperAdmin";
    }

    @PostMapping("/createSuperAdmin")
    public String saveSuperAdmin(@ModelAttribute("SuperAdmin") User user) {
        userService.createUser(user);
        userService.changeRoleToSuperAdmin(user);
        return "redirect:/company/showCompanies";
    }

    @GetMapping("viewSuperAdmins/{companyId}")
    public String viewSuperAdmins(@PathVariable("companyId") long companyId, Model model) {
        List<User> allUsers = companyService.getCompany(companyId).getUsers();
        List<User> superAdmin = new ArrayList<>();
        for (User user : allUsers) {
            List<Role> roles = user.getRoles();
            if (roles.get(0).getRole().equals("SUPERADMIN")) {
                superAdmin.add(user);
            }
        }
        model.addAttribute("superAdmins", superAdmin);
        return "views/superAdminList";

    }
}
