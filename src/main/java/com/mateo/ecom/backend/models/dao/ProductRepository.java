package com.mateo.ecom.backend.models.dao;

import com.mateo.ecom.backend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
