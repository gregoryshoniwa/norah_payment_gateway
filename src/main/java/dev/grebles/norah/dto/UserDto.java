package dev.grebles.norah.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private boolean enabled;
    private String companyName;
    private String userName;
}
