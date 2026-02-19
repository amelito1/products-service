package it.product.product.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "product")
@Setter
@Getter
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(updatable = false)
    private long id;

    @Column(name = "product_name")
    private String productName;

    private Double unitPrice;

    @Positive
    private Long stock;



}
