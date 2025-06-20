package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.DTO.ChangePasswordDTO;
import ru.kata.spring.boot_security.demo.DTO.UserRequestDTO;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void add(User user);

    void delete(Long id);

    void update(User user);

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    List<User> viewAll();

    Optional<User> findByEmail(String email);

    public void changePassword(ChangePasswordDTO dto);
    public String getPassword(Long id);

}

