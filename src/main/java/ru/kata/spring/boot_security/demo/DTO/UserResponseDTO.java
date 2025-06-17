package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String lastName;
    private String email;
    private int age;
    private Set<String> roles;

}

