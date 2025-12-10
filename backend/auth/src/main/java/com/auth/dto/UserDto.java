package com.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private Set<String> roles;

    // getters/setters
}

