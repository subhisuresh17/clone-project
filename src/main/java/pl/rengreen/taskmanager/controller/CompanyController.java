package pl.rengreen.taskmanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.CompanyDto;
import pl.rengreen.taskmanager.model.Note;
import pl.rengreen.taskmanager.service.CompanyService;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/company/addCompany")
    public String addCompany() {
        return "forms/companyForm";
    }

    @PostMapping("/company/addCompany")
    public String addCompanyToDatabase(@ModelAttribute("company") Company company) {
        companyService.addCompany(company);
        return "redirect:/company/showCompanies";
    }

    @GetMapping("/company/showCompanies")
    public String showAllCompanies(Model model) {
        List<Company> getAllCompanies = companyService.getAllCompanies();
        model.addAttribute("companies", getAllCompanies);
        return "views/companyList";
    }

    @GetMapping("/company/allCompanies")
    @ResponseBody
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        List<CompanyDto> allCompanies = companies.stream()
                .map(company -> new CompanyDto(company.getId(), company.getName())).collect(Collectors.toList());
        return allCompanies;
    }
}
