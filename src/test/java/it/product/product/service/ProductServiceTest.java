package it.product.product.service;

import it.product.product.dto.request.OrderedProductRequest;
import it.product.product.dto.request.ProductRequest;
import it.product.product.dto.response.ProductResponse;
import it.product.product.entities.ProductEntity;
import it.product.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest gerProductRequest() {
        final ProductRequest productRequest = new ProductRequest();

        productRequest.setProductName("Laptop");

        productRequest.setPrice(33.0);

        productRequest.setStock(20L);

        return productRequest;
    }

    @Test
    void createProductSuccess() {


        final ProductEntity productSaved = new ProductEntity();

        productSaved.setId(1L);
        productSaved.setStock(20L);
        productSaved.setProductName("Laptop");

        productSaved.setUnitPrice(BigDecimal.valueOf(33.0));

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productSaved);

        final ProductResponse result = this.productService.create(this.gerProductRequest());

        verify(productRepository).save(any(ProductEntity.class));
        assertNotNull(result);

        assertEquals(BigDecimal.valueOf(33.0), result.unitPrice());

        assertEquals(20L, result.stock());
    }

    @Test
    void createProductFails_shouldThrow() {
        doThrow(new RuntimeException("Database unavailable"))
                .when(productRepository)
                .save(any());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> productService.create(this.gerProductRequest()));

        assertEquals("Database unavailable", ex.getMessage());

        verify(productRepository).save(any());
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {

        final ProductEntity product1 = new ProductEntity();

        product1.setId(1L);
        product1.setStock(20L);
        product1.setProductName("Laptop");

        final ProductEntity product2 = new ProductEntity();

        product2.setId(2L);
        product2.setStock(30L);
        product2.setProductName("Laptop2");
        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        var result = this.productService.findProducts();

        assertNotNull(result);

        assertEquals(2, result.size());

        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_whenNoProduct_shouldReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        var result = this.productService.findProducts();

        assertEquals(0, result.size());

        verify(productRepository).findAll();
    }

    @Test
    void findOrderedProductByIds_success() {

        OrderedProductRequest request1 = new OrderedProductRequest();
        request1.setProductId(1L);
        request1.setProductQuantity(20L);

        OrderedProductRequest request2 = new OrderedProductRequest();
        request2.setProductId(2L);
        request2.setProductQuantity(30L);

        List<OrderedProductRequest> requests = List.of(request1, request2);


        ProductEntity entity1 = new ProductEntity();
        entity1.setId(1L);
        entity1.setProductName("Laptop");
        entity1.setStock(200L);
        entity1.setUnitPrice(BigDecimal.valueOf(999.99));

        ProductEntity entity2 = new ProductEntity();
        entity2.setId(2L);
        entity2.setProductName("Mouse");
        entity2.setStock(300L);
        entity2.setUnitPrice(BigDecimal.valueOf(29.99));

        when(productRepository.findByIdIn(List.of(1L, 2L)))
                .thenReturn(List.of(entity1, entity2));

       when(productRepository.decreaseStock(1L,20L )).thenReturn(100);

        when(productRepository.decreaseStock(2L,30L )).thenReturn(100);
        List<ProductResponse> responses = productService.findOrderedProductByIds(requests);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(20, responses.get(0).stock());
        assertEquals(30, responses.get(1).stock());

        assertEquals("Laptop", responses.get(0).productName());
        assertEquals("Mouse", responses.get(1).productName());

        verify(productRepository).findByIdIn(List.of(1L, 2L));
    }


}
