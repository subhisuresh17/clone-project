package pl.rengreen.taskmanager.service;

import pl.rengreen.taskmanager.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User saveUser(User user);

    User changeRoleToAdmin(User user);

    User changeRoleToSuperAdmin(User user);

    User changeRoleToUser(User user);

    List<User> findAll();

    User getUserByEmail(String email);

    boolean isUserEmailPresent(String email);

    User getUserById(Long userId);

    void deleteUser(Long id);

    long countUsers();

    void updateProfilePic(User user, String picUrl);

    void updatePassword(User user, String password);

    public User getUserByVerificationToken(String token);

    public boolean isTokenExpired(User user);
}
