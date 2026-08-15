package com.airbnb.service;

import com.airbnb.dto.PasswordDto;
import com.airbnb.dto.UserDto;
import com.airbnb.dto.UserProfileDto;
import com.airbnb.entity.User;

public interface UserService {

	String createUser(User user);

    User checkUser(UserDto userDto);

	String changePassword(Long userId, PasswordDto passwordDto);

	String updateProfile(Long userId, UserProfileDto userDto);

	User getProfile(Long userId);

}
