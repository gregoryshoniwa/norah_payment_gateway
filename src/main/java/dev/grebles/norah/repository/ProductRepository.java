package dev.grebles.norah.repository;

import dev.grebles.norah.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
