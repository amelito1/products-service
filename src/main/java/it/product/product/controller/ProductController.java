package it.product.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.product.product.dto.request.OrderedProductRequest;
import it.product.product.dto.request.ProductRequest;
import it.product.product.dto.response.ProductResponse;
import it.product.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/product" )
@Tag(name = "Product")
public class ProductController {
    @Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(path = "/create-product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody @Valid ProductRequest productRequest
    ) {
        final  ProductResponse response = this.productService.create(productRequest);
        return new ResponseEntity<ProductResponse>(response, HttpStatus.CREATED);
    }

    @GetMapping(path = "/retrieve-products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> retrieves() {
        final List<ProductResponse> products = this.productService.findProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping(path = "/retrieve-ordered-products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> retrievesOrderedProducts(
            @RequestBody List<OrderedProductRequest>  orderedProductRequests
    ) {
        final List<ProductResponse> products = this.productService.findOrderedProductByIds(orderedProductRequests);
        return ResponseEntity.ok(products);
    }



    @GetMapping(path = "/retrieve-orders-pages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ProductResponse>> retrieveProductsPages(@RequestParam int page,
                                                                   @RequestParam int size) {
        final Page<ProductResponse> productPages = this.productService.retrievesProductPages(page, size);

        return ResponseEntity.ok(productPages);
    }

    @PostMapping(path = "/update-canceled-product-order", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Integer>> updateOrderedCancelProducts(
            @RequestBody List<OrderedProductRequest>  orderedProductRequests
    ){

       final List<Integer> updatedValues = this.productService.updateCanceledProductOrder(orderedProductRequests);
        return ResponseEntity.ok(updatedValues);
    }


}
