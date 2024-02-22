package dev.grebles.norah.repository;

import dev.grebles.norah.entities.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation,Long> {

    Confirmation findByToken(String token);

}
