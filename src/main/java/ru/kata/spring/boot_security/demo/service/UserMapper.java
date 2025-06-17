package ru.kata.spring.boot_security.demo.service;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "password", ignore = true)
    UserRequestDTO toDto(User user);

   // @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    default UserResponseDTO toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));

        return dto;
    }
}