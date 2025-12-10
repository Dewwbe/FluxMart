package com.auth.service;

import com.auth.dto.RegisterRequest;
import com.auth.dto.UserDto;
import com.auth.model.Role;
import com.auth.model.RoleName;
import com.auth.model.User;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository u, RoleRepository r, PasswordEncoder pe) {
        this.userRepo = u;
        this.roleRepo = r;
        this.passwordEncoder = pe;
    }

    public User register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setActive(true);

        // default CUSTOMER (buyer), allow requesting SELLER, etc.
        Set<Role> roles = new HashSet<>();

        if (req.getRoles() == null || req.getRoles().isEmpty()) {
            roles.add(getRole(RoleName.ROLE_CUSTOMER));
        } else {
            for (String r : req.getRoles()) {
                switch (r.toUpperCase()) {
                    case "SELLER" -> roles.add(getRole(RoleName.ROLE_SELLER));
                    case "SHOP_MANAGER" -> roles.add(getRole(RoleName.ROLE_SHOP_MANAGER));
                    case "ADMIN" -> roles.add(getRole(RoleName.ROLE_ADMIN));
                    default -> roles.add(getRole(RoleName.ROLE_CUSTOMER));
                }
            }
        }

        user.setRoles(roles);
        return userRepo.save(user);
    }

    private Role getRole(RoleName name) {
        return roleRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    public UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setEmail(u.getEmail());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setActive(u.isActive());
        dto.setRoles(u.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet()));
        return dto;
    }

    public List<UserDto> findAll() {
        return userRepo.findAll().stream().map(this::toDto).toList();
    }

    public UserDto findById(Long id) {
        return userRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(dto.isActive());

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(r -> RoleName.valueOf(r))
                    .map(this::getRole)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return toDto(userRepo.save(user));
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}