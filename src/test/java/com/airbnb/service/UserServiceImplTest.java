package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.airbnb.dto.PasswordDto;
import com.airbnb.dto.UserDto;
import com.airbnb.dto.UserProfileDto;
import com.airbnb.entity.User;
import com.airbnb.enums.Role;
import com.airbnb.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_validUser_shouldCreateSuccessfully() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        when(userRepo.existsByUName("naresh123"))
                .thenReturn(false);

        when(userRepo.existsByEmail("naresh@gmail.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("Naresh@123"))
                .thenReturn("encodedPassword");

        String result = userService.createUser(user);

        assertEquals(
                "Profile Created Successfully",
                result
        );

        verify(userRepo).save(user);
    }

    @Test
    void createUser_invalidUsername_shouldReturnError() {

        User user = new User();

        user.setuName("abc");
        user.setEmail("naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Username",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_nullUsername_shouldReturnError() {

        User user = new User();

        user.setuName(null);
        user.setEmail("naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Username",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_invalidEmail_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("nareshgmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Email",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_uppercaseEmail_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("Naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Email",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_invalidMobile_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("naresh@gmail.com");
        user.setMobile("98765");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Mobile Number",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_mobileContainsLetter_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("naresh@gmail.com");
        user.setMobile("98765abc10");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@123");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Mobile Number",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_passwordMismatch_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Naresh@123");
        user.setConPassword("Naresh@456");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Password",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_weakPassword_shouldReturnError() {

        User user = new User();

        user.setuName("naresh123");
        user.setEmail("naresh@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("password");
        user.setConPassword("password");
        user.setRole(Role.USER);

        String result = userService.createUser(user);

        assertEquals(
                "Invalid Password",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void createUser_adminRole_shouldNotAllowRegistration() {

        User user = new User();

        user.setuName("admin123");
        user.setEmail("admin@gmail.com");
        user.setMobile("9876543210");
        user.setOriPassword("Admin@123");
        user.setConPassword("Admin@123");
        user.setRole(Role.ADMIN);

        String result = userService.createUser(user);

        assertEquals(
                "Admin Registration Not Allowed",
                result
        );

        verify(userRepo, never()).save(user);
    }

    @Test
    void checkUser_validCredentials_shouldReturnUser() {

        UserDto dto = new UserDto();

        dto.setUsername("naresh123");
        dto.setPassword("Naresh@123");
        dto.setRole(Role.USER);

        User user = new User();

        user.setuName("naresh123");
        user.setOriPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepo.findByUName("naresh123"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "Naresh@123",
                "encodedPassword"
        )).thenReturn(true);

        User result = userService.checkUser(dto);

        assertNotNull(result);

        assertEquals(
                "naresh123",
                result.getuName()
        );

        verify(userRepo).findByUName("naresh123");
    }

    @Test
    void checkUser_userNotFound_shouldReturnNull() {

        UserDto dto = new UserDto();

        dto.setUsername("unknown123");
        dto.setPassword("Naresh@123");
        dto.setRole(Role.USER);

        when(userRepo.findByUName("unknown123"))
                .thenReturn(null);

        User result = userService.checkUser(dto);

        assertNull(result);

        verify(userRepo).findByUName("unknown123");
    }

    @Test
    void checkUser_wrongPassword_shouldReturnNull() {

        UserDto dto = new UserDto();

        dto.setUsername("naresh123");
        dto.setPassword("Wrong@123");
        dto.setRole(Role.USER);

        User user = new User();

        user.setuName("naresh123");
        user.setOriPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepo.findByUName("naresh123"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "Wrong@123",
                "encodedPassword"
        )).thenReturn(false);

        User result = userService.checkUser(dto);

        assertNull(result);
    }

    @Test
    void checkUser_wrongRole_shouldReturnNull() {

        UserDto dto = new UserDto();

        dto.setUsername("naresh123");
        dto.setPassword("Naresh@123");
        dto.setRole(Role.HOST);

        User user = new User();

        user.setuName("naresh123");
        user.setOriPassword("encodedPassword");
        user.setRole(Role.USER);

        when(userRepo.findByUName("naresh123"))
                .thenReturn(user);

        when(passwordEncoder.matches(
                "Naresh@123",
                "encodedPassword"
        )).thenReturn(true);

        User result = userService.checkUser(dto);

        assertNull(result);
    }

    @Test
    void changePassword_validData_shouldChangePassword() {

        Long userId = 1L;

        User user = new User();

        user.setuId(userId);
        user.setOriPassword("oldEncodedPassword");

        PasswordDto dto = new PasswordDto();

        dto.setOldPassword("Old@1234");
        dto.setNewPassword("New@1234");
        dto.setConfirmPassword("New@1234");

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Old@1234",
                "oldEncodedPassword"
        )).thenReturn(true);

        when(passwordEncoder.encode("New@1234"))
                .thenReturn("newEncodedPassword");

        String result =
                userService.changePassword(userId, dto);

        assertEquals(
                "Password Changed Successfully",
                result
        );

        assertEquals(
                "newEncodedPassword",
                user.getOriPassword()
        );

        assertEquals(
                "newEncodedPassword",
                user.getConPassword()
        );

        verify(userRepo).save(user);
    }

    @Test
    void changePassword_userNotFound_shouldReturnError() {

        Long userId = 100L;

        PasswordDto dto = new PasswordDto();

        dto.setOldPassword("Old@1234");
        dto.setNewPassword("New@1234");
        dto.setConfirmPassword("New@1234");

        when(userRepo.findById(userId))
                .thenReturn(Optional.empty());

        String result =
                userService.changePassword(userId, dto);

        assertEquals(
                "User Not Found",
                result
        );

        verify(userRepo, never()).save(any());
    }

    @Test
    void changePassword_wrongOldPassword_shouldReturnError() {

        Long userId = 1L;

        User user = new User();

        user.setuId(userId);
        user.setOriPassword("oldEncodedPassword");

        PasswordDto dto = new PasswordDto();

        dto.setOldPassword("Wrong@1234");
        dto.setNewPassword("New@1234");
        dto.setConfirmPassword("New@1234");

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Wrong@1234",
                "oldEncodedPassword"
        )).thenReturn(false);

        String result =
                userService.changePassword(userId, dto);

        assertEquals(
                "Old Password Incorrect",
                result
        );

        verify(userRepo, never()).save(any());
    }

    @Test
    void changePassword_passwordMismatch_shouldReturnError() {

        Long userId = 1L;

        User user = new User();

        user.setuId(userId);
        user.setOriPassword("oldEncodedPassword");

        PasswordDto dto = new PasswordDto();

        dto.setOldPassword("Old@1234");
        dto.setNewPassword("New@1234");
        dto.setConfirmPassword("Different@1234");

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(user));

        String result =
                userService.changePassword(userId, dto);

        assertEquals(
                "Passwords Do Not Match",
                result
        );

        verify(userRepo, never()).save(any());
    }

    @Test
    void getProfile_existingUser_shouldReturnUser() {

        Long userId = 1L;

        User user = new User();

        user.setuId(userId);
        user.setuName("naresh123");

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(user));

        User result =
                userService.getProfile(userId);

        assertNotNull(result);

        assertEquals(
                "naresh123",
                result.getuName()
        );
    }

    @Test
    void getProfile_userNotFound_shouldReturnNull() {

        Long userId = 100L;

        when(userRepo.findById(userId))
                .thenReturn(Optional.empty());

        User result =
                userService.getProfile(userId);

        assertNull(result);
    }

    @Test
    void updateProfile_validData_shouldUpdateSuccessfully() {

        Long userId = 1L;

        User user = new User();

        user.setuId(userId);
        user.setuName("old123");
        user.setEmail("old@gmail.com");
        user.setMobile("9876543210");

        UserProfileDto dto = new UserProfileDto();

        dto.setFullName("new123");
        dto.setEmail("newuser@gmail.com");
        dto.setPhoneNumber("9123456789");

        when(userRepo.findById(userId))
                .thenReturn(Optional.of(user));

        String result =
                userService.updateProfile(userId, dto);

        assertEquals(
                "Profile Updated Successfully",
                result
        );

        assertEquals(
                "new123",
                user.getuName()
        );

        assertEquals(
                "newuser@gmail.com",
                user.getEmail()
        );

        assertEquals(
                "9123456789",
                user.getMobile()
        );

        verify(userRepo).save(user);
    }

    @Test
    void updateProfile_userNotFound_shouldReturnError() {

        Long userId = 100L;

        UserProfileDto dto = new UserProfileDto();

        dto.setFullName("new123");
        dto.setEmail("newuser@gmail.com");
        dto.setPhoneNumber("9123456789");

        when(userRepo.findById(userId))
                .thenReturn(Optional.empty());

        String result =
                userService.updateProfile(userId, dto);

        assertEquals(
                "User Not Found",
                result
        );

        verify(userRepo, never()).save(any());
    }
}