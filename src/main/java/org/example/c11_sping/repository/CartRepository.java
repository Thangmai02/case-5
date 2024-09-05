package org.example.c11_sping.repository;

import org.example.c11_sping.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
