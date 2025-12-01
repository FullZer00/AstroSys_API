package com.cp.danek.astroAPI.service;

import com.cp.danek.astroAPI.dto.JwtResponseDTO;
import com.cp.danek.astroAPI.dto.LoginRequestDTO;
import com.cp.danek.astroAPI.dto.RegistrationRequestDTO;
import com.cp.danek.astroAPI.dto.UserDTO;
import com.cp.danek.astroAPI.mapper.ModelMapper;
import com.cp.danek.astroAPI.model.entities.Role;
import com.cp.danek.astroAPI.model.entities.User;
import com.cp.danek.astroAPI.model.repositories.RoleRepository;
import com.cp.danek.astroAPI.model.repositories.UserRepository;
import com.cp.danek.astroAPI.security.JwtTokenProvider;
import com.cp.danek.astroAPI.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = tokenProvider;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public JwtResponseDTO authenticate(LoginRequestDTO loginRequest) {
        // Аутентификация пользователя
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерация JWT токена
        String jwt = jwtTokenProvider.generateJwtToken(authentication);

        // Получаем пользователя
        User user = userRepository.findByLogin(loginRequest.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Обновляем время последнего входа
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority()
                .replace("ROLE_", ""); // Убираем префикс ROLE_

        return new JwtResponseDTO(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                role
        );
    }

    public UserDTO register(RegistrationRequestDTO request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new RuntimeException("Пользователь с логином " + request.getLogin() + " уже существует");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = modelMapper.toUserEntity(request);
        user.setRole(role);
        user.setPasswordHash(hashedPassword);

        return modelMapper.toUserDTO(userRepository.save(user));
    }
}
