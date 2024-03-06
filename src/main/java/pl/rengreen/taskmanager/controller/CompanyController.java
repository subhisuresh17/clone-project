package pl.rengreen.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.Note;
import pl.rengreen.taskmanager.service.CompanyService;

@Controller
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;

    @GetMapping("/company/addCompany")
    public String  addCompany() {
        return "forms/companyForm";
    }
    @PostMapping("/company/addCompany")
    public String addCompanyToDatabase(@RequestParam("name") String name){
        Company company=new Company();
        company.setName(name);
        companyService.addCompany(company);
        return "redirect:/company/showCompanies";
    }
    @GetMapping("/company/showCompanies")
    public String showAllCompanies(Model model){
        List<Company> getAllCompanies= companyService.getAllCompanies();
        model.addAttribute("companies", getAllCompanies);
        return "views/companyList";
    }
}
