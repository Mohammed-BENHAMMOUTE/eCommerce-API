package org.sec.ecommerce.controller;
import lombok.RequiredArgsConstructor;
import org.sec.ecommerce.dto.ImageDTO;
import org.sec.ecommerce.model.Image;
import org.sec.ecommerce.response.ApiResponse;
import org.sec.ecommerce.service.image.IImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files , @RequestParam  Long productId){
        try {
            List<ImageDTO> imageDTOS = imageService.addImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("Uploaded", imageDTOS));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
        try {
             Image image = imageService.getImageById(id);
             ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));
             return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())).
                     header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"" + image.getFileName() + "\"").
                     body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId , @RequestBody MultipartFile file){
        try{
            Image image = imageService.getImageById(imageId);
            if(image == null){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found" , null));
            }else{
                imageService.updateImage(file , imageId);
                return ResponseEntity.ok(new ApiResponse("Updated" , null));
            }
        }catch (Exception exception){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(exception.getMessage() , null));
        }
    }
    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteImage(@RequestParam Long imageId){
        try{
            Image image = imageService.getImageById(imageId);
            if(image == null){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found" , NOT_FOUND));
            }
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Deleted" , null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage() , null));
        }
    }
}
