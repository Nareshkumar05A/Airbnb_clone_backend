package com.airbnb.service;

import java.util.List;

import com.airbnb.dto.DashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;

public interface HostService {

    List<Property> getMyProperties(Long userId);

    List<Booking> getBookings(Long userId);

    DashboardDto getDashboard(Long userId);
    
    List<Payment> getPayments(Long userId);

}