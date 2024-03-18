package pl.rengreen.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.rengreen.taskmanager.model.ChatMessage;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.repository.ChatMessageRepository;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Override
    public List<ChatMessage> getChatMessagesBySender(User sender) {
        return messageRepository.findBySender(sender);
    }

    @Override
    public List<ChatMessage> getChatMessagesByRecipient(User recipient) {
        return messageRepository.findByRecipient(recipient);
    }

    @Override
    public void saveChatMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getChatMessagesBySenderOrRecipient(User user) {
        return messageRepository.findBySenderOrRecipient(user, user);
    }

    @Override
    public List<ChatMessage> findMessagesBetweenUsers(User user, User recipient) {
        // Implement logic to find all messages between the user and recipient
        return messageRepository.findBySenderAndRecipientOrSenderAndRecipientOrderByTimestamp(
                user, recipient, recipient, user);
    }

    @Override
    public List<ChatMessage> getMessagesOfSenderToRecipient(User sender, User recipient) {
        return messageRepository.findBySenderAndRecipient(sender, recipient);
    }
}