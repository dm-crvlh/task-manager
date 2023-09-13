package david.td.taskmanager.service;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Employee;
import david.td.taskmanager.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company  findByEmployee(Employee employee){
        return companyRepository.findByEmployees(employee);
    }

}
