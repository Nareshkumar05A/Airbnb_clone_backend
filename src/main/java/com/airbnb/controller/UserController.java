package com.airbnb.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airbnb.dto.PasswordDto;
import com.airbnb.dto.UserDto;
import com.airbnb.dto.UserProfileDto;
import com.airbnb.entity.User;
import com.airbnb.service.UserService;

@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://airbnb-clone-frontend-qh28e2rbv-nareshkumar05as-projects.vercel.app"
})
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userServ;
	
	@PostMapping("/new")
	public String newUser(@RequestBody User user)
	{
		String result = userServ.createUser(user);
		return result;
	}
	
	@PostMapping("/login")
	public User login(@RequestBody UserDto dto){
	    return userServ.checkUser(dto);

	}
	
	@GetMapping("/profile/{userId}")
	public User getProfile(@PathVariable Long userId) {

	    return userServ.getProfile(userId);

	}

	@PutMapping("/update/{userId}")
	public String updateProfile(@PathVariable Long userId,
	                            @RequestBody UserProfileDto userDto) {

	    return userServ.updateProfile(userId, userDto);

	}

	@PatchMapping("/change-password/{userId}")
	public String changePassword(@PathVariable Long userId,
	                             @RequestBody PasswordDto passwordDto) {

	    return userServ.changePassword(userId, passwordDto);

	}
}

