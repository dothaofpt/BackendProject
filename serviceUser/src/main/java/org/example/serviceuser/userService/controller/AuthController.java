package org.example.serviceuser.userService.controller;

import org.example.serviceuser.userService.dto.RegisterCreationRequest;
import org.example.serviceuser.userService.dto.UserDTO;
import org.example.serviceuser.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody RegisterCreationRequest registerRequest) {
        Map<String, Object> response = userService.registerUser(registerRequest);

        if ("Username already exists".equals(response.get("message"))) {
            return ResponseEntity.status(400).body(response);
        }
        if ("Only one ADMIN allowed".equals(response.get("message"))) {
            return ResponseEntity.status(403).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDTO userDTO) {
        Map<String, String> response = userService.loginUser(userDTO);

        if ("Invalid username or password".equals(response.get("message"))) {
            return ResponseEntity.status(401).body(response);
        }
        if ("User not found".equals(response.get("message"))) {
            return ResponseEntity.status(404).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
