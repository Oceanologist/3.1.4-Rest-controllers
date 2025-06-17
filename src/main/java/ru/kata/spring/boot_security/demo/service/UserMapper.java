package ru.kata.spring.boot_security.demo.service;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    UserRequestDTO toDto(User user);

    User toEntity(UserRequestDTO userRequestDTO);


    @Mapping(target = "password", source = "password")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "roles", expression = "java(mapRoles(userResponseDTO.getRoles()))")
    User toEntityFromResponse(UserResponseDTO userResponseDTO);
    @AfterMapping
    default Set<Role> mapRoles(Set<String> roleNames,@MappingTarget User user, @Context RoleService roleService) {
        if (roleNames == null) {
            return Collections.emptySet();
        }
        return roleNames.stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return role;
                })
                .collect(Collectors.toSet());
    }
//    @AfterMapping
//    default void mapRoles(UserResponseDTO dto, @MappingTarget User user, @Context RoleService roleService) {
//        if (dto.getRoles() != null && roleService != null) {
//            Set<Role> roles = dto.getRoles().stream()
//                    .map(roleService::findByName)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .collect(Collectors.toSet());
//            user.setRoles(roles);
//        }
//    }
}


}
