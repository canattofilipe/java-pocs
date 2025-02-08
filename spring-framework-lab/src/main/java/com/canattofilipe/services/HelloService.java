package com.canattofilipe.services;

import org.springframework.stereotype.Component;

@Component
public class HelloService {

    public String hello() {
        return "Hello, Spring Framework!";
    }

}
