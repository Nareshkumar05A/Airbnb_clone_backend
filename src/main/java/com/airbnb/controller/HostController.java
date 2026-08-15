package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.airbnb.dto.DashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.service.HostService;

@RestController
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HostService hostServ;

    @GetMapping("/properties/{userId}")
    public List<Property> myProperties(@PathVariable Long userId){

        return hostServ.getMyProperties(userId);
    }

    @GetMapping("/bookings/{userId}")
    public List<Booking> myBookings(@PathVariable Long userId){

        return hostServ.getBookings(userId);
    }

    @GetMapping("/dashboard/{userId}")
    public DashboardDto dashboard(@PathVariable Long userId){

        return hostServ.getDashboard(userId);
    }
    
    @GetMapping("/payments/{userId}")
    public List<Payment> myPayments(@PathVariable Long userId) {
        return hostServ.getPayments(userId); 
    }

}