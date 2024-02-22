package dev.grebles.norah.repository;

import dev.grebles.norah.entities.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRestRepository extends JpaRepository<PasswordReset,Long> {

    PasswordReset findByToken(String token);

}
