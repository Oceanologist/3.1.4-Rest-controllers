package ru.kata.spring.boot_security.demo.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.DTO.UserResponseDTO;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserMapper;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
@RestController
@RequestMapping("/api")
public class AppRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;


    @Autowired
    public AppRestController(UserService userService, RoleService roleRepository, UserMapper userMapper) {
        this.userService = userService;
        this.roleService = roleRepository;
        this.userMapper = userMapper;

    }

    @PutMapping
            ("/admin/edit")
    @Transactional
    public ResponseEntity<String> updateUser(@RequestBody UserResponseDTO userResponseDTO) {
        if (userService.findByEmail(userResponseDTO.getEmail()).isPresent()) {
            userService.update(userMapper.toEntityFromResponse(userResponseDTO));
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Пользователь обновлен");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не существует либо был удален");
        }


    }



    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteUser(@RequestBody UserResponseDTO userResponseDTO) {
        if (userService.findById(userResponseDTO.getId()).isPresent()) {
            userService.delete(userResponseDTO.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Пользователь успешно удален");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не существует либо был удален.");
    }


    @PostMapping("/admin/new")
    @Transactional
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO) {


        if (userService.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            System.err.println(userRequestDTO.getUsername()+" "+userRequestDTO.getLastName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Пользователь с таким email уже есть");
        } else {
            userService.add(userMapper.toEntity(userRequestDTO));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Информация о пользователе обновлена");
        }


    }
}
//    @GetMapping("/user")
//    public String userPage(Authentication authentication, Model model) {
//        String username = authentication.getName();
//        User user = userService.findByName(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        System.err.println(user.getUsername() + " " + user.getLastName());
//        model.addAttribute("user", user);
//        return "user_page"; // Имя шаблона для страницы пользователя
//    }


//
//    @GetMapping("/admin")
//    public String adminPage(Authentication authentication, Model model) {
//        String username = authentication.getName();
//        User user = userService.findByName(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        model.addAttribute("users", userService.viewAll());
//        return "common-dashboard"; // Имя шаблона для страницы администратора
//    }


//}
