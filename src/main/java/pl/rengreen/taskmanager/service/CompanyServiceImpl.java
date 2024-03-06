package pl.rengreen.taskmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void removeCompany(Company company) {
        companyRepository.delete(company);    
    }

    @Override
    public void updateCompany(long id,Company company) {
        Company ourCompany=companyRepository.findById(id).get();
        ourCompany.setName(company.getName());
        ourCompany.setUsers(company.getUsers());
        companyRepository.save(ourCompany);
    }

    @Override
    public Company getCompany(long id) {
        return  companyRepository.findById(id).get();
    }

    @Override
    public List<User> getCompanyUsers(long id) {
        return companyRepository.findById(id).get().getUsers();
    }

    @Override
    public boolean isUserPresentInCompany(User user,long id) {
        Company company=companyRepository.findById(id).get();
        List<User> allUsers=company.getUsers();
        return allUsers.contains(user);
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();    
    }

    @Override
    public List<Task> getAllTaskByCompany(long id) {
        List<User> allUsers=companyRepository.findById(id).get().getUsers();
        List<Task> task=new ArrayList<>();
        for (User u:allUsers){
            List<Task> tasksOfThisUser=u.getTasksOwned();
            if (!tasksOfThisUser.isEmpty()){
                task.addAll(tasksOfThisUser);
            }
        }
        return task;
    }
    
}
