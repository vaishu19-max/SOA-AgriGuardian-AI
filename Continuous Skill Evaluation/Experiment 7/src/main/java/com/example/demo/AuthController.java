package com.example.demo;

import java.util.Map;

import com.example.demo.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");
        String role;

        if ("admin".equals(username)
                && "admin123".equals(password)) {
            role = "ADMIN";
        } else if ("librarian".equals(username)
                && "lib123".equals(password)) {
            role = "LIBRARIAN";
        } else if ("user".equals(username)
                && "user123".equals(password)) {
            role = "USER";
        } else {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.ok(
            Map.of("token",
                jwtService.generateToken(username, role))
        );
    }
}