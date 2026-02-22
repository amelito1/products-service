package it.product.product.repository;

import it.product.product.entities.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ListCrudRepository<ProductEntity,Long>,
        PagingAndSortingRepository<ProductEntity, Long> {

    List<ProductEntity> findByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query("""
        UPDATE product p
        SET p.stock = p.stock - :qty
        WHERE p.id = :id
        AND p.stock >= :qty
    """)
    int decreaseStock(@Param("id") Long id,
                      @Param("qty") Long qty);

    @Transactional
    @Modifying
    @Query("""
        UPDATE product p
        SET p.stock = p.stock + :qty
        WHERE p.id = :id
        AND p.stock >= :qty
    """)
    int increaseStock(@Param("id") Long id,
                      @Param("qty") Long qty);
}
