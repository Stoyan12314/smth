package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.buisness.UserManager;
import org.example.controller.dto.GetUserResponse;
import org.example.controller.dto.GetUsersResponse;
import org.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserManager userManager;
    @RolesAllowed("ROLE_TRAINER")
    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

        Page<User> users = userManager.getAllUsers(page, size);
        GetUsersResponse response = new GetUsersResponse(users.getContent(), users.getTotalPages());
        return ResponseEntity.ok(response);
    }
    @RolesAllowed("ROLE_TRAINER")
    @GetMapping("/getUsers")
    public ResponseEntity<GetUsersResponse> getUsersByEmail(@RequestParam String email, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<User> users = userManager.getUsersByEmail(email, page, size);
        GetUsersResponse response = new GetUsersResponse(users.getContent(), users.getTotalPages());
        return ResponseEntity.ok(response);
    }
    @GetMapping("{id}")
    public ResponseEntity<GetUserResponse> getUser(@Valid @PathVariable Long id)
    {
        Optional<User> user = userManager.getUser(id);
        if (user.isPresent()) {
            GetUserResponse response = GetUserResponse.builder()
                    .firstName(user.get().getFirstName())
                    .lastName(user.get().getLastName())
                    .email(user.get().getEmail())
                    .password(user.get().getPassword())
                    .build();

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
