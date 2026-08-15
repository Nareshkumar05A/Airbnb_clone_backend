package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.airbnb.dto.AdminDashboardDto;
import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;
import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PaymentRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.WishlishRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PropertyRepository propertyRepo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private WishlishRepository wishRepo;

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User user;
    private Property property;
    private Booking booking;
    private Payment payment;


    @BeforeEach
    void setUp() {

        user = new User();
        user.setuId(1L);
        user.setuName("Naresh123");

        property = new Property();
        property.setPropertyId(10L);
        property.setPropertyName("Beach House");
        property.setUser(user);

        booking = new Booking();
        booking.setBookingId(100L);
        booking.setUser(user);
        booking.setProperty(property);

        payment = new Payment();
        payment.setPaymentId(500L);
        payment.setBooking(booking);
        payment.setAmount(6000);
    }


    @Test
    void getAllUsers_success() {

        List<User> users =
                Arrays.asList(user);

        when(userRepo.findAll())
                .thenReturn(users);

        List<User> result =
                adminService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));

        verify(userRepo).findAll();
    }

    @Test
    void getAllProperties_success() {

        List<Property> properties =
                Arrays.asList(property);

        when(propertyRepo.findAll())
                .thenReturn(properties);

        List<Property> result =
                adminService.getAllProperties();

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));

        verify(propertyRepo).findAll();
    }

    @Test
    void getAllBookings_success() {

        List<Booking> bookings =
                Arrays.asList(booking);

        when(bookingRepo.findAll())
                .thenReturn(bookings);

        List<Booking> result =
                adminService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(booking, result.get(0));

        verify(bookingRepo).findAll();
    }

    @Test
    void getAllPayments_success() {

        List<Payment> payments =
                Arrays.asList(payment);

        when(paymentRepo.findAll())
                .thenReturn(payments);

        List<Payment> result =
                adminService.getAllPayments();

        assertEquals(1, result.size());
        assertEquals(payment, result.get(0));

        verify(paymentRepo).findAll();
    }

    @Test
    void deleteUser_notFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        String result =
                adminService.deleteUser(1L);

        assertEquals("User Not Found", result);

        verify(userRepo, never()).delete(any(User.class));
    }

    @Test
    void deleteUser_success() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookingRepo.findByUser(user))
                .thenReturn(Collections.emptyList());

        when(propertyRepo.findByUser(user))
                .thenReturn(Collections.emptyList());

        String result =
                adminService.deleteUser(1L);

        assertEquals(
                "User Deleted Successfully",
                result
        );

        verify(reviewRepo).deleteByUser(user);
        verify(wishRepo).deleteByUser(user);
        verify(userRepo).deleteById(1L);
    }

    @Test
    void deleteUser_withBookings() {

        List<Booking> bookings =
                Arrays.asList(booking);

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookingRepo.findByUser(user))
                .thenReturn(bookings);

        when(propertyRepo.findByUser(user))
                .thenReturn(Collections.emptyList());

        String result =
                adminService.deleteUser(1L);

        assertEquals(
                "User Deleted Successfully",
                result
        );

        verify(paymentRepo).deleteByBookingIn(bookings);
        verify(bookingRepo).deleteByUser(user);
        verify(userRepo).deleteById(1L);
    }

    @Test
    void deleteProperty_notFound() {

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.empty());

        String result =
                adminService.deleteProperty(10L);

        assertEquals(
                "Property Not Found",
                result
        );

        verify(propertyRepo, never())
                .delete(any(Property.class));
    }

    @Test
    void deleteProperty_success() {

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(bookingRepo.findByProperty(property))
                .thenReturn(Collections.emptyList());

        String result =
                adminService.deleteProperty(10L);

        assertEquals(
                "Property Deleted Successfully",
                result
        );

        verify(wishRepo).deleteByProperty(property);
        verify(reviewRepo).deleteByProperty(property);
        verify(bookingRepo).deleteByProperty(property);
        verify(propertyRepo).delete(property);
    }

    @Test
    void getDashboard_success() {

        when(userRepo.findAll())
                .thenReturn(Arrays.asList(user));

        when(propertyRepo.findAll())
                .thenReturn(Arrays.asList(property));

        when(bookingRepo.findAll())
                .thenReturn(Arrays.asList(booking));

        when(paymentRepo.findAll())
                .thenReturn(Arrays.asList(payment));

        AdminDashboardDto result =
                adminService.getDashboard();

        assertNotNull(result);

        assertEquals(1, result.getTotalUsers());
        assertEquals(1, result.getTotalProperties());
        assertEquals(1, result.getTotalBookings());
        assertEquals(1, result.getTotalPayments());
        assertEquals(6000, result.getTotalRevenue());
    }

    @Test
    void getDashboard_noPayments() {

        when(userRepo.findAll())
                .thenReturn(Collections.emptyList());

        when(propertyRepo.findAll())
                .thenReturn(Collections.emptyList());

        when(bookingRepo.findAll())
                .thenReturn(Collections.emptyList());

        when(paymentRepo.findAll())
                .thenReturn(Collections.emptyList());

        AdminDashboardDto result =
                adminService.getDashboard();

        assertNotNull(result);

        assertEquals(0, result.getTotalUsers());
        assertEquals(0, result.getTotalProperties());
        assertEquals(0, result.getTotalBookings());
        assertEquals(0, result.getTotalPayments());
        assertEquals(0, result.getTotalRevenue());
    }

    @Test
    void updateUserStatus_activate() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        String result =
                adminService.updateUserStatus(1L, true);

        assertEquals(
                "User Activated Successfully",
                result
        );

        assertTrue(user.isActive());

        verify(userRepo).save(user);
    }

    @Test
    void updateUserStatus_deactivate() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        String result =
                adminService.updateUserStatus(1L, false);

        assertEquals(
                "User Deactivated Successfully",
                result
        );

        assertFalse(user.isActive());

        verify(userRepo).save(user);
    }

    @Test
    void updateUserStatus_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        String result =
                adminService.updateUserStatus(1L, true);

        assertEquals(
                "User Not Found",
                result
        );

        verify(userRepo, never())
                .save(any(User.class));
    }
}