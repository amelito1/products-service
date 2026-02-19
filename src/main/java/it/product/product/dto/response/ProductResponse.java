package it.product.product.dto.response;

import jakarta.validation.constraints.NotNull;

public record ProductResponse(
        @NotNull long id,
        @NotNull String productName,
        @NotNull Long stock,
        @NotNull Double unitPrice
) {
}
