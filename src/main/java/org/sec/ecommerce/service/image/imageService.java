package org.sec.ecommerce.service.image;
import lombok.RequiredArgsConstructor;
import org.sec.ecommerce.Exceptions.RessourceNotFoundException;
import org.sec.ecommerce.Repository.ImageRepository;
import org.sec.ecommerce.dto.ImageDTO;
import org.sec.ecommerce.model.Image;
import org.sec.ecommerce.model.Product;
import org.sec.ecommerce.service.product.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class imageService implements IImageService {
    private final ImageRepository imageRepository;
    private final  IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(image -> {
            imageRepository.delete(image);
        }, () -> {
            throw new RessourceNotFoundException("Image not found");
        });
    }
    @Override
    public List<ImageDTO> addImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        String baseDownloadUrl = "/api/v1/images/image/download/";
        List<ImageDTO> savedImagesDTOs = new ArrayList<>();
    
        for (MultipartFile file : files) {
            try {
                Image savedImage = createAndSaveImage(file, product);
                savedImage.setDownloadUrl(baseDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);
                savedImagesDTOs.add(convertToDto(savedImage));
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImagesDTOs;
    }
    
    // Helper method to build and save an Image
    private Image createAndSaveImage(MultipartFile file, Product product) throws IOException, SQLException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));
        image.setProduct(product);
        return imageRepository.save(image);
    }
    
    // Helper method to convert Image to ImageDTO
    private ImageDTO convertToDto(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setImageName(image.getFileName());
        dto.setImageId(image.getId());
        dto.setDownloadUrl(image.getDownloadUrl());
        return dto;
    }
    
    @Override
    public Image updateImage(MultipartFile file, Long id) {
        Image image = getImageById(id);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RessourceNotFoundException("Image not found");
        }
    }
}
