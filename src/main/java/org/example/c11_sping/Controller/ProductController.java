package org.example.c11_sping.Controller;

import org.example.c11_sping.model.Product;
import org.example.c11_sping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("Name is required"), HttpStatus.BAD_REQUEST);
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            return new ResponseEntity<>(new ErrorResponse("Price must be positive"), HttpStatus.BAD_REQUEST);
        }
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            return new ResponseEntity<>(new ErrorResponse("Category is required"), HttpStatus.BAD_REQUEST);
        }

        try {
            Product savedProduct = productRepository.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    if (updatedProduct.getName() != null) product.setName(updatedProduct.getName());
                    if (updatedProduct.getDescription() != null) product.setDescription(updatedProduct.getDescription());
                    if (updatedProduct.getPrice() != null) product.setPrice(updatedProduct.getPrice());
                    if (updatedProduct.getCategory() != null) product.setCategory(updatedProduct.getCategory());
                    if (updatedProduct.getImages() != null) product.setImages(updatedProduct.getImages());
                    Product savedProduct = productRepository.save(product);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
