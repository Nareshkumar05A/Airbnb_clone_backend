package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.airbnb.dto.AdminDashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminServ;

    @GetMapping("/users")
    public List<User> getUsers() {

        return adminServ.getAllUsers();
    }

    @GetMapping("/properties")
    public List<Property> getProperties() {

        return adminServ.getAllProperties();
    }

    @GetMapping("/bookings")
    public List<Booking> getBookings() {

        return adminServ.getAllBookings();
    }

    @GetMapping("/payments")
    public List<Payment> getPayments() {

        return adminServ.getAllPayments();
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {

        return adminServ.deleteUser(id);
    }

    @DeleteMapping("/property/{id}")
    public String deleteProperty(@PathVariable Long id) {

        return adminServ.deleteProperty(id);
    }

    @GetMapping("/dashboard")
    public AdminDashboardDto dashboard() {

        return adminServ.getDashboard();
    }
    
    @PatchMapping("/user/{id}/status")
    public String updateUserStatus(@PathVariable Long id, @RequestParam boolean active) {

        return adminServ.updateUserStatus(id, active);
    }

}