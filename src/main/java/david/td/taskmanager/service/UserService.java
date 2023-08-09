package david.td.taskmanager.service;

import david.td.taskmanager.model.User;

public interface UserService {
    void registerUser(User user);
    User findByUsername(String username);
    boolean authenticateUser(String username, String password);
}