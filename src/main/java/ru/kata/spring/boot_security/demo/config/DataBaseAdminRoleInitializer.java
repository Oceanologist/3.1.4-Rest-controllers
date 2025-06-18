package ru.kata.spring.boot_security.demo.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Component
public class DataBaseAdminRoleInitializer implements CommandLineRunner {
    private final RoleService roleService;
    private final UserService userService;


    @Autowired
    public DataBaseAdminRoleInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;

    }

    @Override
    public void run(String... args) throws Exception {
        initAdminAndUserRoles();
        initAdmin();
    }

    @Transactional
    public void initAdminAndUserRoles() {
        if (!roleService.existsByName("ROLE_ADMIN")) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleService.addRole(role);
        }
        if (!roleService.existsByName("ROLE_USER")) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleService.addRole(role);
        }
    }

    @Transactional
    void initAdmin() {
        if (userService.findByName("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setAge(30);
            admin.setEmail("admin@example.com");
            admin.setLastName("Adminov");

            Role adminRole = roleService.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

            admin.addRole(adminRole);
            userService.add(admin);

        }
    }

}
