package org.sec.ecommerce.dto;
import lombok.Data;

@Data
public class ImageDTO {
    private Long ImageId;
    private String fileName;
    private String downloadUrl;
}