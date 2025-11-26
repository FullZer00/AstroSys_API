package com.cp.danek.astroAPI.model.repositories;

import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск по логину
    Optional<User> findByLogin(String login);

    // Поиск по email
    Optional<User> findByEmail(String email);

    // Поиск по роли
    List<User> findByRole_Name(UserRole roleName);

    // Поиск активных пользователей
    List<User> findByIsActiveTrue();

    // Поиск по имени (частичное совпадение)
    List<User> findByFullNameContainingIgnoreCase(String name);

    // Проверка существования логина
    boolean existsByLogin(String login);

    // Проверка существования email
    boolean existsByEmail(String email);

    // Поиск пользователей по нескольким ролям
    @Query("SELECT u FROM User u WHERE u.role.name IN :roles")
    List<User> findByRoles(@Param("roles") List<UserRole> roles);
}