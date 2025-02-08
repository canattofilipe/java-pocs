package com.canattofilipe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.canattofilipe.config.AppConfig;
import com.canattofilipe.services.HelloService;

public class App {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        final var bean = context.getBean(HelloService.class);

        System.out.println(bean.hello());

    }
}
