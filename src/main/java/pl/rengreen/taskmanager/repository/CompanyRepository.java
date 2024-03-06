package pl.rengreen.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
