package com.enviro.assessment.junior.sellwane.repository;

import com.enviro.assessment.junior.sellwane.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
