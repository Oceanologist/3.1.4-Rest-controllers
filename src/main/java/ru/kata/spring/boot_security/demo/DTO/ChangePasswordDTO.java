package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private Long UserId;
    private String currentPassword;
    private String newPassword;
}
