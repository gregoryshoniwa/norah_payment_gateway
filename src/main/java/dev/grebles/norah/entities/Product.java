package dev.grebles.norah.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private String productDescription;
    private String productPrice;
    private String productImage;
    private String productStatus;
    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType productType;
    // join products to one merchant
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

}
