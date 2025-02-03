package org.sec.ecommerce.Repository;

import org.sec.ecommerce.model.Category;
import org.sec.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByBrand(String brand);
  List<Product> findByCategoryName(String category);
  List<Product> findByCategoryNameAndBrand(String category, String brand);

  List<Product> findByName(String name);

  List<Product> findByBrandAndName(String brand, String name);

  Long countByBrandAndName(String brand, String name);
}