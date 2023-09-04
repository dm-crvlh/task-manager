package david.td.taskmanager.repository;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByUsername(String username);

    List<Employee> findAllByCompany(Company company);
}
