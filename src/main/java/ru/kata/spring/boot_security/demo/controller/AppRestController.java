package ru.kata.spring.boot_security.demo.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.ChangePasswordDTO;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.service.UserMapperService;
import ru.kata.spring.boot_security.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class AppRestController {
    private final UserService userService;

    private final UserMapperService userMapperService;

    @Autowired
    public AppRestController(UserService userService, UserMapperService userMapperService) {
        this.userService = userService;
        this.userMapperService = userMapperService;
    }

    @Transactional
    @PutMapping("/admin/edit")
    public ResponseEntity<String> updateUser(@RequestBody UserResponseDTO userResponseDTO) {
        if (userService.findByEmail(userResponseDTO.getEmail()).isPresent()) {
            userService.update(userMapperService.mapFromResponse(userResponseDTO));
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Пользователь обновлен");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не существует либо был удален");
        }

    }

    @PostMapping("/admin/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.findById(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не существует либо был удален.");
    }


    @PostMapping("/admin/new")
    @Transactional
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO) {

        if (userService.findByEmail(userRequestDTO.getEmail()).isPresent()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь с таким email уже есть");
        } else {
            userService.add(userMapperService.toEntity(userRequestDTO));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Информация о пользователе обновлена");
        }

    }
}
