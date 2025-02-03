package org.sec.ecommerce.request;

import lombok.Data;
import org.sec.ecommerce.model.Category;
import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "category_id")
    private Category category;

}
