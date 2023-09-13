package david.td.taskmanager.service;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Employee;
import david.td.taskmanager.model.Role;
import david.td.taskmanager.repository.CompanyRepository;
import david.td.taskmanager.repository.RoleRepository;
import david.td.taskmanager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(Employee employee, Long selectedCompanyId) {
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        employee.setRoles(Collections.singletonList(userRole));
        Company company = companyRepository.findById(selectedCompanyId).orElse(null);
        if (company != null) {
            employee.setCompany(company);
        }
        System.out.println(employee.getRoles().get(0).getName());
        employeeRepository.save(employee);
    }


    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String password) {
        Employee employee = findByUsername(username);
        if (employee != null && passwordEncoder.matches(password, employee.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : employee.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }
        return false;
    }

    public List<Employee> findAllUsersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company != null) {
            return employeeRepository.findAllByCompany(company);
        }
        return Collections.emptyList(); // Retourne une liste vide si l'entreprise n'est pas trouvée
    }

    public Employee getUserById(Long userId) {
        return employeeRepository.findById(userId).orElse(null);
    }

    public boolean isUsernameAlreadyUsed(String username) {
        Employee existingEmployee = employeeRepository.findByUsername(username);
        return existingEmployee != null;
    }

    public boolean isEmailAlreadyUsed(String email) {
        Employee existingEmployee = employeeRepository.findByEmail(email);
        return existingEmployee != null;
    }
}