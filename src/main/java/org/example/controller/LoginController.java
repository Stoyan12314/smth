package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.buisness.LoginManager;
import org.example.controller.dto.LoginRequest;
import org.example.controller.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final LoginManager loginManager;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = loginManager.login(loginRequest);
        if (loginResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(loginResponse);
    }
}


