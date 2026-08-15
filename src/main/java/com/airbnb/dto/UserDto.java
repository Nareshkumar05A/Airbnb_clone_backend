package com.airbnb.dto;

import com.airbnb.enums.Role;

import lombok.Data;

@Data
public class UserDto {

    private String username;
    private String password;
    private Role role;
    

}
