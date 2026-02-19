package it.product.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotNull
    private String productName;
    @NotNull
    private Long stock;

    @NotNull
    private Double price;
}
