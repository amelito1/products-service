package it.product.product.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderedProductRequest {
    private Long productId;
    private Long productQuantity;
    private BigDecimal productUnitPrice;
}
