package it.product.product.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "product")
@Table(name = "product")
@Setter
@Getter
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(updatable = false)
    private long id;

    @Column(name = "product_name")
    private String productName;

    private BigDecimal unitPrice;

    @Positive
    private Long stock;



}
