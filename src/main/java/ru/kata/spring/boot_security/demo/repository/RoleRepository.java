package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    default boolean existsByName(String roleName) {
        return findByName(roleName).isPresent();
    }

    Optional<Role> findByName(String name);

    Set<Role> findAllByNameIn(Collection<String> names);

}