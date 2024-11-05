package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.models.Product;
import com.mateo.ecom.backend.models.dao.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getProducts(){
        return productRepository.findAll();
    }
}
