package it.product.product.service;

import it.euris.common.PageUtils;
import it.product.product.dto.request.OrderedProductRequest;
import it.product.product.dto.request.ProductRequest;
import it.product.product.dto.response.ProductResponse;
import it.product.product.entities.ProductEntity;
import it.product.product.repository.ProductRepository;
import it.product.product.utility.ProductUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(ProductRequest productRequest) {
        final ProductEntity productEntity = ProductUtility.mapProductToEntity(productRequest);

        final ProductEntity productSaved =  this.productRepository.save(productEntity);

        return ProductUtility.mapProductToResponse(productSaved);
    }

    public List<ProductResponse> findProducts() {
        final List<ProductResponse> productEntities = this.productRepository
                .findAll()
                .stream()
                .map(ProductUtility::mapProductToResponse).toList();
        return productEntities;
    }

    public List<ProductResponse> findOrderedProductByIds(List<OrderedProductRequest> products) {

        final List<Long> productIds = products.stream().map(OrderedProductRequest::getProductId).toList();

        final List<ProductEntity> productEntity = this.productRepository.findByIdIn(productIds);

        this.checkIfProductExists(productIds, productEntity);

        this.reduceStocks(products);

        List<ProductEntity> productEntityWithOrderedQuantities= IntStream
                .range(0, products.size()).mapToObj(i -> {
            final ProductEntity product = productEntity.get(i);
            final OrderedProductRequest orderedProductRequest = products.get(i);
            product.setStock(orderedProductRequest.getProductQuantity());
            return product;
        } ).toList();

        return productEntityWithOrderedQuantities.stream().map(ProductUtility::mapProductToResponse).toList() ;

    }

    public List<Integer> updateCanceledProductOrder(List<OrderedProductRequest> orderedProductRequests) {

      return  this.increaseStocks(orderedProductRequests);
    }

    public Page<ProductResponse> retrievesProductPages(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        final List<ProductResponse> customers = this.productRepository
                .findAll(pageable)
                .stream()
                .map(ProductUtility::mapProductToResponse).toList();
        return PageUtils.toPage(customers, pageable);
    }



    private void  checkIfProductExists(List<Long> productIds, List<ProductEntity> products) {
        if (productIds.isEmpty() || productIds.size() != productIds.size() ) {

            throw new RuntimeException("Some products do not exist");
        }
    }


    private void reduceStocks(List<OrderedProductRequest> products) {

        for (OrderedProductRequest  product: products) {
            int updated = productRepository.decreaseStock(
                    product.getProductId(),
                    product.getProductQuantity()
            );
            if (updated == 0) {
                throw new RuntimeException("Insufficient stock");
            }
        }
    }

    private List<Integer> increaseStocks(List<OrderedProductRequest> products) {

        List<Integer> updatedValue = new ArrayList<>(products.size());

        for (OrderedProductRequest  product: products) {
            int updated = productRepository.decreaseStock(
                    product.getProductId(),
                    product.getProductQuantity()
            );
            updatedValue.add(updated);
        }

        return updatedValue;
    }


}
