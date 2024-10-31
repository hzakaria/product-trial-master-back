package product.trial.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product.trial.model.Product;

import java.util.List;


@RequestMapping("/api/v1/products")
public interface ProductApi {

    @PostMapping
    ResponseEntity<Product> createProduct(@RequestBody Product product);

    @GetMapping
    ResponseEntity<List<Product>> getAllProducts();

    @GetMapping("/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id);

    @PatchMapping("/{id}")
    ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails);

    @GetMapping("/status")
    ResponseEntity<String> getStatus();
}
