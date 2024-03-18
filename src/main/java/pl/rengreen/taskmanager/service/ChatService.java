package pl.rengreen.taskmanager.service;

import java.util.List;

import pl.rengreen.taskmanager.model.ChatMessage;
import pl.rengreen.taskmanager.model.User;

public interface ChatService {
    List<ChatMessage> getChatMessagesBySender(User sender);

    List<ChatMessage> getChatMessagesByRecipient(User recipient);

    void saveChatMessage(ChatMessage message);

    public List<ChatMessage> getChatMessagesBySenderOrRecipient(User user);

    List<ChatMessage> findMessagesBetweenUsers(User user, User recipient);

    List<ChatMessage> getMessagesOfSenderToRecipient(User sender, User recipient);
}
