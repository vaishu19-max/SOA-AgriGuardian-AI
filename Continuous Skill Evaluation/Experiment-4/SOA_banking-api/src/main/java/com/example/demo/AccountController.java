package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final JwtUtil jwtUtil;

    public AccountController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/details")
    public ResponseEntity<?> accountDetails(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization) {

        // Check if token exists
        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(401)
                    .body("Unauthorized: Token missing");
        }

        // Remove "Bearer "
        String token = authorization.substring(7);

        // Validate token
        if (!jwtUtil.validateToken(token)) {

            return ResponseEntity
                    .status(401)
                    .body("Unauthorized: Invalid or expired token");
        }

        // Get username
        String username = jwtUtil.extractUsername(token);

        return ResponseEntity.ok(
                "Account Details for " + username +
                ": Balance = ₹50,000"
        );
    }
}