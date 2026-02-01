package task.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import task.example.demo.entity.AppUser;
import task.example.demo.repository.UserRepository;
import task.example.demo.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(String username, String password) {
        if (username == null || username.isBlank()) throw new RuntimeException("Username required");
        if (password == null || password.length() < 6) throw new RuntimeException("Password too short");
        if (userRepository.existsByUsername(username)) throw new RuntimeException("Username already exists");

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public String login(String username, String password) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user.getId(), user.getUsername());
    }
}
