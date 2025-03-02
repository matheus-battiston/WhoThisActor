package com.MovieParticipations.MovieParticipations.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/isAuthenticated")
public class IsAuthenticatedController {

    @GetMapping
    public ResponseEntity<?> checkAuthentication(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(UNAUTHORIZED).body("User is not authenticated");
        }
        return ResponseEntity.ok("User is authenticated as: " + principal.getName());
    }
}