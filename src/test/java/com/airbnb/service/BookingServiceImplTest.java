package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
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

import com.airbnb.entity.Booking;
import com.airbnb.entity.BookingStatus;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.repository.BookingRepository;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private PropertyRepository proRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private BookingRepository bookRepo;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Property property;
    private Booking booking;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setuId(1L);
        user.setuName("Naresh123");

        property = new Property();
        property.setPropertyId(10L);
        property.setPropertyName("Beach House");
        property.setPricePerNight(2000);
        property.setMaxGuests(4);
        property.setBedrooms(2);
        property.setBathrooms(2);
        property.setAvailable(true);
        property.setUser(user);

        booking = new Booking();

        booking.setBookingId(100L);
        booking.setCheckInDate(LocalDate.of(2026, 8, 20));
        booking.setCheckOutDate(LocalDate.of(2026, 8, 23));
        booking.setNumberOfGuests(2);
        booking.setUser(user);
        booking.setProperty(property);
    }

    @Test
    void createBook_success() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(proRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(bookRepo.findByProperty(property))
                .thenReturn(Collections.emptyList());

        String result = bookingService.createBook(booking);

        assertEquals("Booking Created Successfully", result);

        assertEquals(6000, booking.getTotalPrice());

        assertEquals(BookingStatus.PENDING,
                booking.getBookingStatus());

        verify(bookRepo).save(booking);
    }



    @Test
    void createBook_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        String result = bookingService.createBook(booking);

        assertEquals("User Not Found", result);

        verify(bookRepo, never()).save(any(Booking.class));
    }


    @Test
    void createBook_propertyNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(proRepo.findById(10L))
                .thenReturn(Optional.empty());

        String result = bookingService.createBook(booking);

        assertEquals("Property Not Found", result);

        verify(bookRepo, never()).save(any(Booking.class));
    }



    @Test
    void createBook_invalidDates() {

        booking.setCheckInDate(
                LocalDate.of(2026, 8, 25));

        booking.setCheckOutDate(
                LocalDate.of(2026, 8, 20));

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(proRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(bookRepo.findByProperty(property))
                .thenReturn(Collections.emptyList());

        String result = bookingService.createBook(booking);

        assertEquals("Invalid Booking Dates", result);

        verify(bookRepo, never()).save(any(Booking.class));
    }


    @Test
    void createBook_sameDates() {

        booking.setCheckInDate(
                LocalDate.of(2026, 8, 20));

        booking.setCheckOutDate(
                LocalDate.of(2026, 8, 20));

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(proRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(bookRepo.findByProperty(property))
                .thenReturn(Collections.emptyList());

        String result = bookingService.createBook(booking);

        assertEquals("Invalid Booking Dates", result);

        verify(bookRepo, never()).save(any(Booking.class));
    }


    @Test
    void createBook_propertyAlreadyBooked() {

        Booking existingBooking = new Booking();

        existingBooking.setCheckInDate(
                LocalDate.of(2026, 8, 21));

        existingBooking.setCheckOutDate(
                LocalDate.of(2026, 8, 25));

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(proRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(bookRepo.findByProperty(property))
                .thenReturn(Arrays.asList(existingBooking));

        String result = bookingService.createBook(booking);

        assertEquals(
                "Property is already booked for these dates",
                result);

        verify(bookRepo, never()).save(any(Booking.class));
    }


    @Test
    void cancelBooking_success() {

        booking.setBookingStatus(BookingStatus.PENDING);

        when(bookRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        String result =
                bookingService.cancelBooking(100L);

        assertEquals(
                "Booking Cancelled Successfully",
                result);

        assertEquals(
                BookingStatus.CANCELLED,
                booking.getBookingStatus());

        verify(bookRepo).save(booking);
    }


    @Test
    void cancelBooking_notFound() {

        when(bookRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                bookingService.cancelBooking(100L);

        assertEquals("Booking Not Found", result);

        verify(bookRepo, never())
                .save(any(Booking.class));
    }



    @Test
    void cancelBooking_alreadyCancelled() {

        booking.setBookingStatus(
                BookingStatus.CANCELLED);

        when(bookRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        String result =
                bookingService.cancelBooking(100L);

        assertEquals(
                "Booking Already Cancelled",
                result);

        verify(bookRepo, never())
                .save(any(Booking.class));
    }


    @Test
    void viewMyBookings_success() {

        List<Booking> bookings =
                Arrays.asList(booking);

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookRepo.findByUser(user))
                .thenReturn(bookings);

        List<Booking> result =
                bookingService.viewMyBookings(1L);

        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals(booking, result.get(0));

        verify(bookRepo).findByUser(user);
    }



    @Test
    void viewMyBookings_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        List<Booking> result =
                bookingService.viewMyBookings(1L);

        assertNull(result);

        verify(bookRepo, never())
                .findByUser(any(User.class));
    }


    @Test
    void getBookingById_success() {

        when(bookRepo.findById(100L))
                .thenReturn(Optional.of(booking));

        Booking result =
                bookingService.getBookingById(100L);

        assertNotNull(result);

        assertEquals(100L,
                result.getBookingId());
    }


    @Test
    void getBookingById_notFound() {

        when(bookRepo.findById(100L))
                .thenReturn(Optional.empty());

        Booking result =
                bookingService.getBookingById(100L);

        assertNull(result);
    }
}