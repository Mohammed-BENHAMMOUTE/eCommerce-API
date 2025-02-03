package org.sec.ecommerce.service.product;

import org.sec.ecommerce.model.Product;
import org.sec.ecommerce.request.AddProductRequest;

import java.util.List;

public interface IProductService {
//    Product addProduct(Product product);

    Product addProduct(AddProductRequest product);

    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
     Product updateProduct(Product product , Long id);
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category , String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand , String name);
    Long countProductByBrandAndName(String brand, String name);
}
