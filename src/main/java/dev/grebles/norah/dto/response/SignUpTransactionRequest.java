package dev.grebles.norah.dto.response;

import lombok.Data;

@Data
public class SignUpTransactionRequest {

    private String merchantName;
    private String merchantAddress;
    private String merchantPhone;
    private String merchantEmail;
    private String merchantStatus;
    private String merchantSecret;
    private String merchantType;
    private String merchantCountry;
    private String merchantCity;
    private String merchantLogo;
    private String merchantWebsite;
    private String merchantDescription;
}
