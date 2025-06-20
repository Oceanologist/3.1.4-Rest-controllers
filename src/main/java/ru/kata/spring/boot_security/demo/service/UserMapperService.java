package ru.kata.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapperService {

    private final RoleService roleService;
    private final UserService userService;

    public User mapFromResponse(UserResponseDTO userResponseDTO) {
        User user = new User();
        user.setId(userResponseDTO.getId());
        user.setUsername(userResponseDTO.getUsername());
        user.setLastName(userResponseDTO.getLastName());
        user.setEmail(userResponseDTO.getEmail());
        user.setAge(userResponseDTO.getAge());
        user.setPassword(userService.getPassword(userResponseDTO.getId()));


        if (userResponseDTO.getRoles() != null) {
            Set<Role> roles = userResponseDTO.getRoles().stream()
                    .map(roleService::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }

    public User toEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        user.setAge(userRequestDTO.getAge());
        user.setPassword(userRequestDTO.getPassword());
        Set<String> roleNames = userRequestDTO.getRoles();


        if (userRequestDTO.getRoles() != null) {
            Set<Role> roles = userRequestDTO.getRoles().stream()
                    .map(roleService::findByName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }
}