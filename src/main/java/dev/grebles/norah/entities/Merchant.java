package dev.grebles.norah.entities;

import dev.grebles.norah.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "merchants")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long merchantId;

    private String merchantName;
    private String merchantAddress;
    private String merchantPhone;
    private String merchantEmail;
    private String merchantSecret;
    private MerchantStatus merchantStatus;
    private String merchantCountry;
    private String merchantCity;
    private String merchantLogo;
    private String merchantWebsite;
    private String merchantDescription;
    // join the user id to the merchant
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
