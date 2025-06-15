package ru.kata.spring.boot_security.demo.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface RoleService {
    default boolean existsByName(String roleName) {
        return findByName(roleName).isPresent();
    }

    Optional<Role> findByName(String name);

    Set<Role> findAllByNameIn(Collection<String> names);

    void addRole(Role role);

    public List<Role> getAllRoles();

}
