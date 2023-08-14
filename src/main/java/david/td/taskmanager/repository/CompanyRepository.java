package david.td.taskmanager.repository;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findAll();

    Company findByEmployees(User user);
}
