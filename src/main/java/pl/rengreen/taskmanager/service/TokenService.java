package pl.rengreen.taskmanager.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    // Generate a random token
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
