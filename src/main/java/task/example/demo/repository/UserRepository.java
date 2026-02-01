package task.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import task.example.demo.entity.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
