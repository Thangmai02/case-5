package org.example.c11_sping.Controller;

import org.example.c11_sping.model.Cart;
import org.example.c11_sping.model.CartItem;
import org.example.c11_sping.model.Product;
import org.example.c11_sping.repository.CartRepository;
import org.example.c11_sping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        return cart.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{cartId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        Optional<Product> productOptional = productRepository.findById(productId);

        if (cartOptional.isPresent() && productOptional.isPresent()) {
            Cart cart = cartOptional.get();
            Product product = productOptional.get();

            CartItem cartItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cartItem = new CartItem(product, quantity);
                cart.getItems().add(cartItem);
            }

            cart.setTotalPrice(cart.calculateTotalPrice());
            cartRepository.save(cart);

            return new ResponseEntity<>(cart, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{cartId}/update")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .ifPresent(item -> {
                        item.setQuantity(quantity);
                        if (item.getQuantity() <= 0) {
                            cart.getItems().remove(item);
                        }
                    });

            cart.setTotalPrice(cart.calculateTotalPrice());
            cartRepository.save(cart);

            return new ResponseEntity<>(cart, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{cartId}/remove")
    public ResponseEntity<Cart> removeCartItem(@PathVariable Long cartId, @RequestParam Long productId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

            cart.setTotalPrice(cart.calculateTotalPrice());
            cartRepository.save(cart);

            return new ResponseEntity<>(cart, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
