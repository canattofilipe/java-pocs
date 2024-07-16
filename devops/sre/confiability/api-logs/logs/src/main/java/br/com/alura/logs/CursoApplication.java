package br.com.alura.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CursoApplication {

  public static void main(String[] args) {

    log.info("Starting application...");
    SpringApplication.run(CursoApplication.class, args);
    log.info("Application started!");
  }
}
