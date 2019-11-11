package com.example;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/greeting")
public class GreetingController {

    private GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    public Greeting hello(@RequestParam(defaultValue = "world")String name) {

        return greetingService.greet(name);
    }
}