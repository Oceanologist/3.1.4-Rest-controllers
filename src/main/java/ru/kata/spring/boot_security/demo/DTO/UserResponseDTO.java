package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;
@Component
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String lastName;
    private String email;
    private int age;
    private String password;
    private Set<String> roles;

}

