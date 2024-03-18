package pl.rengreen.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.rengreen.taskmanager.model.ChatMessage;
import pl.rengreen.taskmanager.model.User;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySender(User sender);
    // You can define custom query methods if needed

    List<ChatMessage> findByRecipient(User recipient);

    List<ChatMessage> findBySenderOrRecipient(User user, User user2);

    List<ChatMessage> findBySenderAndRecipientOrSenderAndRecipientOrderByTimestamp(
            User user1, User user2, User user3, User user4);

    List<ChatMessage> findBySenderAndRecipient(User sender, User reciepient);
}
