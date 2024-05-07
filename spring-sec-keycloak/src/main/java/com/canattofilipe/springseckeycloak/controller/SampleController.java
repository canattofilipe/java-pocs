package com.canattofilipe.springseckeycloak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("/private/content")
  public ResponseEntity<String> getPrivateContent() {
    return ResponseEntity.ok("<h1> Private Content </h1>");
  }

  @GetMapping("/public/content")
  public ResponseEntity<String> getPublicContent() {
    return ResponseEntity.ok("<h1> Private Content </h1>");
  }
}
