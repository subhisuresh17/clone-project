package pl.rengreen.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.rengreen.taskmanager.model.Role;
import pl.rengreen.taskmanager.model.Task;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.RoleRepository;
import pl.rengreen.taskmanager.repository.TaskRepository;
import pl.rengreen.taskmanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final String SUPER_ADMIN = "SUPERADMIN";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
            TaskRepository taskRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByRole(USER);
        user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User changeRoleToSuperAdmin(User user) {
        Role superAdminRole = roleRepository.findByRole(SUPER_ADMIN);
        user.setRoles(new ArrayList<>(Collections.singletonList(superAdminRole)));
        return userRepository.save(user);
    }

    @Override
    public User changeRoleToAdmin(User user) {
        Role adminRole = roleRepository.findByRole(ADMIN);
        user.setRoles(new ArrayList<>(Collections.singletonList(adminRole)));
        return userRepository.save(user);
    }

    @Override
    public User changeRoleToUser(User user) {
        Role userRole = roleRepository.findByRole(USER);
        user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isUserEmailPresent(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.getOne(id);
        user.getTasksOwned().forEach(task -> task.setOwner(null));
        userRepository.delete(user);
    }

    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public void updateProfilePic(User user, String picUrl) {
        user.setPhoto(picUrl);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(password);
        userRepository.save(user);
    }

    public User getUserByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token);
    }

    public boolean isTokenExpired(User user) {
        return user.getTokenExpiryDate().isBefore(LocalDateTime.now());
    }

}
