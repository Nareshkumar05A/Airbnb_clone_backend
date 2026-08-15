package com.airbnb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.dto.AdminDashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.WishlishRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private BookingRepository bookingRepo;
    
    @Autowired
    private WishlishRepository wishRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Override
    public List<User> getAllUsers() {

        return userRepo.findAll();
    }

    @Override
    public List<Property> getAllProperties() {

        return propertyRepo.findAll();
    }

    @Override
    public List<Booking> getAllBookings() {

        return bookingRepo.findAll();
    }

    @Override
    public List<Payment> getAllPayments() {

        return paymentRepo.findAll();
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {

        User user = userRepo.findById(userId).orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        reviewRepo.deleteByUser(user);

        wishRepo.deleteByUser(user);

        List<Booking> userBookings = bookingRepo.findByUser(user);

        if (!userBookings.isEmpty()) {
            paymentRepo.deleteByBookingIn(userBookings);
            bookingRepo.deleteByUser(user);
        }

        List<Property> properties = propertyRepo.findByUser(user);

        for (Property property : properties) {

            List<Booking> bookings = bookingRepo.findByProperty(property);

            if (!bookings.isEmpty()) {
                paymentRepo.deleteByBookingIn(bookings);
                bookingRepo.deleteByProperty(property);
            }

            reviewRepo.deleteByProperty(property);

            wishRepo.deleteByProperty(property);

            propertyRepo.delete(property);
        }

        userRepo.deleteById(userId);

        return "User Deleted Successfully";
    }

    @Override
    @Transactional
    public String deleteProperty(Long propertyId) {

        Property property = propertyRepo.findById(propertyId).orElse(null);

        if (property == null) {
            return "Property Not Found";
        }

        List<Booking> bookings = bookingRepo.findByProperty(property);

        if (!bookings.isEmpty()) {
            paymentRepo.deleteByBookingIn(bookings);
        }

        bookingRepo.deleteByProperty(property);

        wishRepo.deleteByProperty(property);

        reviewRepo.deleteByProperty(property);

        propertyRepo.delete(property);

        return "Property Deleted Successfully";
    }
    
    @Override
    public AdminDashboardDto getDashboard() {

        AdminDashboardDto dto = new AdminDashboardDto();

        dto.setTotalUsers(userRepo.findAll().size());

        dto.setTotalProperties(propertyRepo.findAll().size());

        dto.setTotalBookings(bookingRepo.findAll().size());

        dto.setTotalPayments(paymentRepo.findAll().size());

        int revenue = 0;

        List<Payment> payments = paymentRepo.findAll();

        for(Payment payment : payments)
        {
            revenue += payment.getAmount();
        }

        dto.setTotalRevenue(revenue);

        return dto;
    }
    
    @Override
    public String updateUserStatus(Long userId, boolean active) {

        User user = userRepo.findById(userId).orElse(null);

        if(user == null)
            return "User Not Found";

        user.setActive(active);
        userRepo.save(user);

        return active ? "User Activated Successfully" : "User Deactivated Successfully";
    }

}