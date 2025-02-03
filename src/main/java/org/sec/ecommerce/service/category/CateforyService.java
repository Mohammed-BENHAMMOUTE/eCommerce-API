package org.sec.ecommerce.service.category;

import lombok.AllArgsConstructor;
import org.sec.ecommerce.Exceptions.AlreadyExistsException;
import org.sec.ecommerce.Exceptions.RessourceNotFoundException;
import org.sec.ecommerce.Repository.CategoryRepository;
import org.sec.ecommerce.model.Category;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service @AllArgsConstructor
public class CateforyService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new RessourceNotFoundException("Category not found with id: " + id)
        );
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(cat -> !categoryRepository.existsByName(cat.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new AlreadyExistsException("Category already exists"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
        return categoryRepository.findById(id)
                .map(oldCategory -> {
                            oldCategory.setName(category.getName());
                            return categoryRepository.save(oldCategory);
                        }
                ).orElseThrow(
                        () -> new RessourceNotFoundException("Category not found with id: " + id)
                );
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(
                        category -> categoryRepository.delete(category),
                        () -> {
                            throw new RessourceNotFoundException("Category not found with id: " + id);
                        }
                );

    }
}
