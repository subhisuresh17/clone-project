package pl.rengreen.taskmanager.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmailVerificationRequiredEventListener {

    @EventListener
    public void handleEmailVerificationRequiredEvent(EmailVerificationRequiredEvent event) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getResponse();
        try {
            response.sendRedirect("/register/verify-email");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the redirection exception
        }
    }
}
