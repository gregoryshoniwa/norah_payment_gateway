package dev.grebles.norah.dto.request;

import lombok.Data;

@Data
public class RestPassword {
    private String email;
    private String oldPassword;
    private String newPassword;
}
