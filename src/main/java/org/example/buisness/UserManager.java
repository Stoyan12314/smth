package org.example.buisness;

import org.example.domain.User;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface UserManager {
        Page<User> getAllUsers(int page, int size);
        Page<User> getUsersByEmail(String email, int page, int size);
        Optional<User> getUser(Long id);

}
