package org.sec.ecommerce.Exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productNotFoundMessage) {
        super(productNotFoundMessage);
    }
}
