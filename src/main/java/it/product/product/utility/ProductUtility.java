package it.product.product.utility;

import it.product.product.dto.request.ProductRequest;
import it.product.product.dto.response.ProductResponse;
import it.product.product.entities.ProductEntity;

public class ProductUtility {

    static public  ProductEntity mapProductToEntity(ProductRequest productRequest) {

        final ProductEntity product = new ProductEntity();

        product.setProductName(productRequest.getProductName());

        product.setStock(productRequest.getStock());
        product.setUnitPrice(productRequest.getPrice());

        return product;
    }


    static public ProductResponse mapProductToResponse(ProductEntity productEntity) {
        return new ProductResponse(
                productEntity.getId(),
                productEntity.getProductName(),
                productEntity.getStock(),
                productEntity.getUnitPrice());
    }
}
