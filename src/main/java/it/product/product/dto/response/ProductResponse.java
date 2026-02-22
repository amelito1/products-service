package it.product.product.dto.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductResponse(
        @NotNull long id,
        @NotNull String productName,
        @NotNull Long stock,
        @NotNull BigDecimal unitPrice
) {
}
