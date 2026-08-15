package com.airbnb.service;

import java.util.List;

import com.airbnb.dto.AdminDashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;

public interface AdminService {

    List<User> getAllUsers();

    List<Property> getAllProperties();

    List<Booking> getAllBookings();

    List<Payment> getAllPayments();

    String deleteUser(Long userId);

    String deleteProperty(Long propertyId);

    AdminDashboardDto getDashboard();
    
    
    
    String updateUserStatus(Long userId, boolean active);

}