package org.acme.spring.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private String message;

    public GreetingService(@Value("${greeting.message}") String message) {
        this.message = message;
    }

    public Greeting greet(String name) {
        return new Greeting(message + " " + name);
    }
}
