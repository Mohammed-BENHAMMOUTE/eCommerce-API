package org.sec.ecommerce.controller;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.sec.ecommerce.dto.ProductDto;
import org.sec.ecommerce.model.Product;
import org.sec.ecommerce.request.AddProductRequest;
import org.sec.ecommerce.request.UpdateProductRequest;
import org.sec.ecommerce.response.ApiResponse;
import org.sec.ecommerce.service.product.IProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<Product> products =  productService.getAllProducts();
            List<ProductDto> productDTOs = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products fetched successfully", productDTOs));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable long productId) {
        try{
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.converToDTO(product);
            return ResponseEntity.ok(new ApiResponse("Product fetched successfully", productDto));
        } catch (Exception exception){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String productName) {
        try {
            List<Product> products = productService.getProductByName(productName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDTOs = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Product fetched successfully", productDTOs));
        } catch (Exception exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            ProductDto productDto = productService.converToDTO(newProduct);
            return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable long productId, @RequestBody UpdateProductRequest product) {
        try {
            Product updatedProduct = productService.updateProduct(product, productId);
            ProductDto productDto = productService.converToDTO(updatedProduct);
            return ResponseEntity.ok(new ApiResponse("Product updated successfully", productDto));
        } catch (Exception exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", productId));
        } catch (Exception exception) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("/name/{productName}/brand/{brandName}")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@PathVariable String brandName , @PathVariable String productName) {
        try {

            List<Product> products = productService.getProductByBrandAndName(brandName, productName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Product fetched successfully", productDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(),null));
        }
    }

    @GetMapping("/brand/{brandName}/category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByBrandAndCategory(@PathVariable String brandName, @PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(categoryName , brandName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products fetched successfully", productDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("brand/{brandName}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brandName) {
        try {
            List<Product> products = productService.getProductByBrand(brandName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Products fetched successfully", products));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String categoryName) {
        try {
            List<Product> products = productService.getProductByCategory(categoryName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", null));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products fetched successfully", productDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), null));
        }
    }

    @GetMapping("brand/{brandName}/name/{productName}/count")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@PathVariable String brandName , @PathVariable String productName) {
        try {
            Long  count = productService.countProductByBrandAndName(brandName, productName);
            return ResponseEntity.ok(new ApiResponse("Product count fetched successfully", count));
        } catch (Exception exception) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage(), null));
        }
    }
}