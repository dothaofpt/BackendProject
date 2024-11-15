package org.example.productservice.serviceProduct.controller;

import org.example.productservice.serviceProduct.dto.ProductDTO;
import org.example.productservice.serviceProduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createProduct(
            @RequestHeader("username") String username,
            @RequestBody ProductDTO productDTO) {

        Map<String, String> response = new HashMap<>();

        if (productDTO.getCategoryId() == null) {
            response.put("error", "Category ID cannot be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            int result = productService.createProduct(productDTO);

            if (result == 1) {
                response.put("message", "Product created successfully by " + username);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("message", "Product creation failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryId(@PathVariable Integer categoryId) {
        try {
            List<ProductDTO> products = productService.getProductsByCategoryId(categoryId);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@RequestHeader("username") String username, @PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            int result = productService.updateProduct(id, productDTO);
            if (result == 1) {
                response.put("message", "Product updated successfully by " + username);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Product update failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@RequestHeader("username") String username, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            int result = productService.deleteProduct(id);
            if (result == 1) {
                response.put("message", "Product deleted successfully by " + username);
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Product deletion failed");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
