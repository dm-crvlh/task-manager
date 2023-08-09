package david.td.taskmanager.service;

import david.td.taskmanager.model.User;
import david.td.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String password) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }
}
