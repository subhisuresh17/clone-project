package pl.rengreen.taskmanager.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.rengreen.taskmanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByResetToken(String token);

    User findByVerificationToken(String token);

    List<User> findByTokenExpiryDateBefore(Date date);
}
