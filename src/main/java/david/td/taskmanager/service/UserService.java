package david.td.taskmanager.service;

import david.td.taskmanager.model.Company;
import david.td.taskmanager.model.Role;
import david.td.taskmanager.model.User;
import david.td.taskmanager.repository.CompanyRepository;
import david.td.taskmanager.repository.RoleRepository;
import david.td.taskmanager.repository.UserRepository;
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
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(User user, Long selectedCompanyId) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        user.setRoles(Collections.singletonList(userRole));
        Company company = companyRepository.findById(selectedCompanyId).orElse(null);
        if (company != null) {
            user.setCompany(company);
        }
        System.out.println(user.getRoles().get(0).getName());
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String password) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }
        return false;
    }

    public List<User> findAllUsersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company != null) {
            return userRepository.findAllByCompany(company);
        }
        return Collections.emptyList(); // Retourne une liste vide si l'entreprise n'est pas trouvée
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}