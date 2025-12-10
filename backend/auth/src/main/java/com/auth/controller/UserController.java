package com.auth.controller;

import com.auth.dto.UserDto;
import com.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService us) {
        this.userService = us;
    }

    /**
     * Get all users
     * Accessible by ADMIN and SHOP_MANAGER
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Get one user by ID
     * Accessible by ADMIN and SHOP_MANAGER
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
    public UserDto getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Update a user
     * Accessible by ADMIN and SHOP_MANAGER
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SHOP_MANAGER')")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        return userService.updateUser(id, dto);
    }

    /**
     * Delete a user
     * Only ADMIN can delete users
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
