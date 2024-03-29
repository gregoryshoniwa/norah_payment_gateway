package dev.grebles.norah.repository;

import dev.grebles.norah.entities.User;
import dev.grebles.norah.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    User findByEmailIgnoreCase(String email);
    User findByRole(Role role);
    Boolean existsByEmail(String email);

}
