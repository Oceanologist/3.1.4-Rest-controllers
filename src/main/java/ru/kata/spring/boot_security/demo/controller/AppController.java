package ru.kata.spring.boot_security.demo.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;


@RequestMapping("/view")
@Controller
public class AppController {
    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AppController(UserService userService, RoleService roleRepository) {
        this.userService = userService;
        this.roleService = roleRepository;


    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String exeptionMethod() {
        return "access-denied";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String doAvtorisation(Authentication authentication, Model model) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("users", userService.viewAll());
            return "common-dashboard";
        } else {
            User user = userService.findByName(authentication.getName()).orElseThrow(() -> new RuntimeException("Ошибка поиска пользователя в методе doAvtorisation"));
            model.addAttribute("user", user);
            return "user_page";

        }
    }


    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user_page"; // Имя шаблона для страницы пользователя
    }


    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("users", userService.viewAll());
        return "common-dashboard";
    }


}
