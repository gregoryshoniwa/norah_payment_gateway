package dev.grebles.norah.repository;

import dev.grebles.norah.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    List<Merchant> findByUserId(Long userId);
}
