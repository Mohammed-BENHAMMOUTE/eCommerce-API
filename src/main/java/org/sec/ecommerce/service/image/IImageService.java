package org.sec.ecommerce.service.image;

import org.sec.ecommerce.dto.ImageDTO;
import org.sec.ecommerce.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> addImage(List<MultipartFile> file, Long productId);
    Image updateImage(MultipartFile file , Long id);
}
