package org.example.c11_sping.repository;

import org.example.c11_sping.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    List<Product> findByCategory_Name(String category); // assuming you have a Category entity with a 'name' field
    List<Product> findByNameContainingAndCategory_Name(String name, String category);
}