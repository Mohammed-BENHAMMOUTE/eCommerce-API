package org.sec.ecommerce.Repository;

import org.sec.ecommerce.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProduct_Id(Long productId);
}