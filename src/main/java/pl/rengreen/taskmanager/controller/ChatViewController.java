package pl.rengreen.taskmanager.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.rengreen.taskmanager.model.ChatMessage;
import pl.rengreen.taskmanager.model.MessageDto;
import pl.rengreen.taskmanager.model.User;
import pl.rengreen.taskmanager.model.UserDto;
import pl.rengreen.taskmanager.service.ChatService;
import pl.rengreen.taskmanager.service.CompanyService;
import pl.rengreen.taskmanager.service.UserService;

@Controller
@RequestMapping("/chat")
public class ChatViewController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatService chatService;
    private final CompanyService companyService;

    @Autowired
    public ChatViewController(UserService userService, ChatService chatService, CompanyService companyService) {
        this.userService = userService;
        this.chatService = chatService;
        this.companyService = companyService;
    }

    public UserDto userMapper(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName(), user.getPhoto());
        return userDto;
    }

    @GetMapping("/messages")
    public String chatMessages(Principal principal, Model model) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        List<User> employees = companyService.getCompanyUsers(user.getCompany().getId());
        List<UserDto> allEmployee = new ArrayList<>();
        for (User e : employees) {
            allEmployee.add(userMapper(e));
        }
        model.addAttribute("Users", allEmployee);
        model.addAttribute("signedIn", user);
        return "Chat/ChatMessages";
    }

    @GetMapping("/messages/{recipientId}")
    @ResponseBody
    public HashMap<String, List<MessageDto>> getChatMessages(@PathVariable("recipientId") Long recipientId, Model model,
            Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        User reciepient = userService.getUserById(recipientId);
        List<ChatMessage> messagesBySender = chatService.getMessagesOfSenderToRecipient(user, reciepient);
        List<ChatMessage> messagesByRecipient = chatService.getMessagesOfSenderToRecipient(reciepient, user);
        List<MessageDto> senderMessages = new ArrayList<>();
        List<MessageDto> recipientMessages = new ArrayList<>();
        for (ChatMessage message : messagesBySender) {
            senderMessages.add(messageMapper(message));
        }
        for (ChatMessage message : messagesByRecipient) {
            recipientMessages.add(messageMapper(message));
        }
        HashMap<String, List<MessageDto>> map = new HashMap<>();
        map.put("Sender", senderMessages);
        map.put("Recipient", recipientMessages);
        return map;
    }

    @GetMapping("/senderName/{senderId}")
    @ResponseBody
    public ResponseEntity<String> viewName(@PathVariable("senderId") long senderId) {
        User user = userService.getUserById(senderId);
        return ResponseEntity.ok(user.getName());
    }
    // @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/public")
    // public ChatMessage sendMessages(@Payload ChatMessage chatMessage) {
    // chatService.saveChatMessage(chatMessage);
    // return chatMessage;
    // }

    // @MessageMapping("/send")
    // public void sendMessage(ChatMessage message) {
    // chatService.saveChatMessage(message);
    // messagingTemplate.convertAndSendToUser(
    // message.getRecipient().getName(),
    // "/queue/messages",
    // message);
    // }

    // @SubscribeMapping("/messages")
    // public List<ChatMessage> getChatMessages(Principal principal) {
    // String email = principal.getName();
    // User user = userService.getUserByEmail(email);
    // return chatService.getChatMessagesBySenderOrRecipient(user);
    // }

    public MessageDto messageMapper(ChatMessage message) {
        MessageDto messageDto = new MessageDto(message.getId(), message.getSender().getName(),
                message.getRecipient().getName(), message.getContent(), message.getTimestamp());
        return messageDto;
    }

    // @MessageMapping("/history")
    // public void getChatHistory(@Payload Long userId) {
    // User user = userService.getUserById(userId);
    // if (user != null) {
    // List<ChatMessage> messages =
    // chatService.getChatMessagesBySenderOrRecipient(user);
    // messagingTemplate.convertAndSendToUser(user.getName(), "/queue/history",
    // messages);
    // }
    // }

}
