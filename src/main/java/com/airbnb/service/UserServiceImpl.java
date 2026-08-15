package com.airbnb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.airbnb.dto.PasswordDto;
import com.airbnb.dto.UserDto;
import com.airbnb.dto.UserProfileDto;
import com.airbnb.entity.User;
import com.airbnb.enums.Role;
import com.airbnb.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String createUser(User user) {

        if (!checkUserName(user.getuName())) {
            return "Invalid Username";
        }

        if (!checkEmail(user.getEmail())) {
            return "Invalid Email";
        }

        if (!checkMobile(user.getMobile())) {
            return "Invalid Mobile Number";
        }

        if (!checkPassword(
                user.getOriPassword(),
                user.getConPassword())) {
            return "Invalid Password";
        }

        if (user.getRole() == Role.ADMIN) {
            return "Admin Registration Not Allowed";
        }

        if (userRepo.existsByUName(user.getuName())) {
            return "Username Already Exists";
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            return "Email Already Exists";
        }

        String encodedPassword =
                passwordEncoder.encode(user.getOriPassword());

        user.setOriPassword(encodedPassword);
        user.setConPassword(encodedPassword);

        userRepo.save(user);

        return "Profile Created Successfully";
    }

    private boolean checkEmail(String email) {

        if (email == null || email.length() < 12) {
            return false;
        }

        if (Character.isDigit(email.charAt(0))
                || email.charAt(0) == '.') {
            return false;
        }

        boolean hasAt = false;

        for (int i = 0; i < email.length(); i++) {

            char ch = email.charAt(i);

            if (Character.isUpperCase(ch)) {
                return false;
            }

            if (ch == '@') {

                hasAt = true;

                if (!email.substring(i).equals("@gmail.com")) {
                    return false;
                }

                break;
            }
        }

        return hasAt;
    }

    private boolean checkMobile(String mobile) {

        if (mobile == null || mobile.length() != 10) {
            return false;
        }

        for (int i = 0; i < mobile.length(); i++) {

            if (!Character.isDigit(mobile.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPassword(
            String password,
            String confirmPassword) {

        if (password == null || confirmPassword == null) {
            return false;
        }

        if (!password.equals(confirmPassword)) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;

        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                upper = true;
            } else if (Character.isLowerCase(ch)) {
                lower = true;
            } else if (Character.isDigit(ch)) {
                digit = true;
            } else {
                special = true;
            }
        }

        return upper && lower && digit && special;
    }

    private boolean checkUserName(String uName) {

        if (uName == null || uName.length() < 5) {
            return false;
        }

        boolean letter = false;
        boolean digit = false;

        for (int i = 0; i < uName.length(); i++) {

            char ch = uName.charAt(i);

            if (Character.isLetter(ch)) {
                letter = true;
            } else if (Character.isDigit(ch)) {
                digit = true;
            }
        }

        return letter && digit;
    }

    @Override
    public User checkUser(UserDto userDto) {

        User user = userRepo.findByUName(
                userDto.getUsername());

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches(
                userDto.getPassword(),
                user.getOriPassword())) {
            return null;
        }

        if (user.getRole() != userDto.getRole()) {
            return null;
        }

        return user;
    }

    @Override
    public String changePassword(
            Long userId,
            PasswordDto dto) {

        User user = userRepo.findById(userId)
                .orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        // First check new password and confirm password
        if (!dto.getNewPassword()
                .equals(dto.getConfirmPassword())) {

            return "Passwords Do Not Match";
        }

        // Then check old password
        if (!passwordEncoder.matches(
                dto.getOldPassword(),
                user.getOriPassword())) {

            return "Old Password Incorrect";
        }

        // Encode the new password
        String encodedPassword =
                passwordEncoder.encode(dto.getNewPassword());

        user.setOriPassword(encodedPassword);
        user.setConPassword(encodedPassword);

        userRepo.save(user);

        return "Password Changed Successfully";
    }

    @Override
    public String updateProfile(
            Long userId,
            UserProfileDto dto) {

        User user = userRepo.findById(userId)
                .orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        user.setuName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getPhoneNumber());

        userRepo.save(user);

        return "Profile Updated Successfully";
    }

    @Override
    public User getProfile(Long userId) {

        return userRepo.findById(userId)
                .orElse(null);
    }
}