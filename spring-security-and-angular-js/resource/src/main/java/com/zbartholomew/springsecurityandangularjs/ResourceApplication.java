package com.zbartholomew.springsecurityandangularjs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
@RestController
public class ResourceApplication {

    // CORS protocol
    // origins = "*" is not secure and should not be used in prod
    @RequestMapping("/")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public Message home() {
        return new Message("Hello World");
    }

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }

}

class Message {
    private String id = UUID.randomUUID().toString();
    private String content;

    Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
