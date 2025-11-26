package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.Role;
import com.cp.danek.astroAPI.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Поиск роли по имени
    Optional<Role> findByName(UserRole name);

    // Проверка существования роли по имени
    boolean existsByName(UserRole name);
}