package org.example.productservice.serviceProduct.service;

import org.example.productservice.serviceProduct.dto.ProductDTO;
import org.example.productservice.serviceProduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public int createProduct(ProductDTO productDTO) {
        // Kiểm tra categoryId hợp lệ (chỉ nhận categoryId là 1 hoặc 2)
        if (productDTO.getCategoryId() == null || (productDTO.getCategoryId() != 1 && productDTO.getCategoryId() != 2)) {
            throw new IllegalArgumentException("Invalid category_id. It must be either 1 or 2.");
        }
        return productRepository.createProduct(productDTO);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryId(Integer categoryId) {
        // Kiểm tra categoryId hợp lệ
        if (categoryId == null || (categoryId != 1 && categoryId != 2)) {
            throw new IllegalArgumentException("Invalid category_id. It must be either 1 or 2.");
        }
        return productRepository.getProductsByCategoryId(categoryId);
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        ProductDTO product = productRepository.getProductById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        return product;
    }

    @Override
    public int updateProduct(Integer id, ProductDTO productDTO) {
        // Kiểm tra categoryId hợp lệ khi cập nhật
        if (productDTO.getCategoryId() == null || (productDTO.getCategoryId() != 1 && productDTO.getCategoryId() != 2)) {
            throw new IllegalArgumentException("Invalid category_id. It must be either 1 or 2.");
        }
        return productRepository.updateProduct(id, productDTO);
    }

    @Override
    public int deleteProduct(Integer id) {
        return productRepository.deleteProduct(id);
    }
}
