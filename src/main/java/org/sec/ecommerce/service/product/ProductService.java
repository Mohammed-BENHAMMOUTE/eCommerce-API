package org.sec.ecommerce.service.product;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.sec.ecommerce.Exceptions.ProductNotFoundException;
import org.sec.ecommerce.Repository.CategoryRepository;
import org.sec.ecommerce.Repository.ImageRepository;
import org.sec.ecommerce.Repository.ProductRepository;
import org.sec.ecommerce.dto.ImageDTO;
import org.sec.ecommerce.dto.ProductDto;
import org.sec.ecommerce.model.Category;
import org.sec.ecommerce.model.Image;
import org.sec.ecommerce.model.Product;
import org.sec.ecommerce.request.AddProductRequest;
import org.sec.ecommerce.request.UpdateProductRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService  implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final  ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest product) {
        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName())).
                orElseGet(()-> {
                    Category  newCategory = new Category(product.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        product.setCategory(category);
        return productRepository.save(createProduct(product , category));
    }

    private Product createProduct(AddProductRequest request , Category category)
    {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }


    @Override
    public void deleteProductById(Long id) {
        productRepository
                .findById(id)
                .ifPresentOrElse(productRepository::delete ,
                        () -> { throw new ProductNotFoundException("Product not found");
                });
    }


    @Override
    public Product updateProduct(UpdateProductRequest product, Long id) {
        return productRepository.findById(id)
                .map( existingProduct -> updateExistingProduct(existingProduct , product))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    };


    private Product updateExistingProduct(Product existingProduct , UpdateProductRequest request)
    {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName())).orElseGet(() ->{
            Category newCategory  = new Category(request.getCategory().getName());
            return categoryRepository.save(newCategory);
        });
        existingProduct.setCategory(category);
        return existingProduct;
    };


    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);

    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products
                .stream()
                .map(this::converToDTO)
                .toList();
    }

    @Override
    public ProductDto converToDTO(Product product) {
        ProductDto productDto =  modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProduct_Id((product.getId()));
        List<ImageDTO> imageDTOS = images
                .stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .toList();
        productDto.setImages(imageDTOS);
        return productDto;
    }
}