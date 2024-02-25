package pl.rengreen.taskmanager.configuration;

import org.springframework.context.ApplicationEvent;

public class EmailVerificationRequiredEvent extends ApplicationEvent {

    private final String username;

    public EmailVerificationRequiredEvent(String username) {
        super(username);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
