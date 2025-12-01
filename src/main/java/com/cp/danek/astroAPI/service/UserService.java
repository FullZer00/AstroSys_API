package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.dto.RegistrationRequestDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Role;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.enums.UserRole;
import com.cp.danek.astroAPI.model.repositories.UserRepository;
import com.cp.danek.astroAPI.model.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Основные CRUD операции
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User createUser(User user) {
        // Базовая валидация
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new RuntimeException("Пользователь с логином " + user.getLogin() + " уже существует");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullName(userDetails.getFullName());
                    user.setEmail(userDetails.getEmail());
                    user.setPhone(userDetails.getPhone());
                    user.setIsActive(userDetails.getIsActive());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Бизнес-логика
    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole_Name(role);
    }

    public List<User> getActiveAstronomers() {
        return userRepository.findByRole_Name(UserRole.ASTRONOMER);
    }

    public List<User> getActiveEngineers() {
        return userRepository.findByRole_Name(UserRole.ENGINEER);
    }

    public void deactivateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setIsActive(false);
            userRepository.save(user);
        });
    }
}