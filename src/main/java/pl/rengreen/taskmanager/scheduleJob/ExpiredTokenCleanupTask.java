package pl.rengreen.taskmanager.scheduleJob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Component
public class ExpiredTokenCleanupTask {

    private final UserRepository userRepository;

    public ExpiredTokenCleanupTask(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    public void deleteUsersWithExpiredTokens() {
        Date currentDate = new Date();
        List<User> users = userRepository.findByTokenExpiryDateBefore(currentDate);
        userRepository.deleteAll(users);
    }
}
