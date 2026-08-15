package com.airbnb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.dto.DashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;

@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PaymentRepository paymentRepo;
    

    @Override
    public List<Property> getMyProperties(Long userId) {

        User user = userRepo.findById(userId).orElse(null);

        if(user == null)
            return null;

        return propertyRepo.findByUser(user);
    }

    @Override
    public List<Booking> getBookings(Long userId) {

        User user = userRepo.findById(userId).orElse(null);

        if(user == null)
            return null;

        List<Property> properties = propertyRepo.findByUser(user);

        return bookingRepo.findByPropertyIn(properties);
    }

    @Override
    public DashboardDto getDashboard(Long userId) {

        User user = userRepo.findById(userId).orElse(null);

        if(user == null)
            return null;

        List<Property> properties = propertyRepo.findByUser(user);

        List<Booking> bookings = bookingRepo.findByPropertyIn(properties);

        List<Payment> payments = paymentRepo.findByBookingIn(bookings);

        DashboardDto dto = new DashboardDto();

        dto.setTotalProperties(properties.size());

        dto.setTotalBookings(bookings.size());

        dto.setAvailableProperties(
                propertyRepo.countByUserAndAvailable(user,true));

        int earnings = 0;

        for(Payment payment : payments)
        {
            earnings += payment.getAmount();
        }

        dto.setTotalEarnings(earnings);

        return dto;
    }
    
    @Override
    public List<Payment> getPayments(Long userId) {
        return paymentRepo.findByBookingPropertyUserUId(userId);
    }

}