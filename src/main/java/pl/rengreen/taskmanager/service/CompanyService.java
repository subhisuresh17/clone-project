package pl.rengreen.taskmanager.service;

import java.util.List;

import pl.rengreen.taskmanager.model.Company;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;

public interface CompanyService {
    
    void addCompany(Company company);
    void removeCompany(Company company);
    void updateCompany(long id,Company company);
    Company getCompany(long id);
    List<User> getCompanyUsers(long id);
    List<Company> getAllCompanies();
    boolean isUserPresentInCompany(User user,long company_id);
    List<Task> getAllTaskByCompany(long id);
}
