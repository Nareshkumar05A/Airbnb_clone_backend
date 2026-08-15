package com.airbnb.service;

import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.entity.Booking;
import com.airbnb.entity.BookingStatus;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.repository.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private PropertyRepository proRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BookingRepository bookRepo;

	
	@Override
	public String createBook(Booking booking) {
		
		User user = userRepo.findById(
	            booking.getUser().getuId()).orElse(null);

	    if(user == null)
	    {
	        return "User Not Found";
	    }

	    Property property = proRepo.findById(
	            booking.getProperty().getPropertyId()).orElse(null);

	    if(property == null)
	    {
	        return "Property Not Found";
	    }

	    List<Booking> bookingList = bookRepo.findByProperty(property);
	    
	    for (Booking existingBooking : bookingList)
		{
	    	if (booking.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
	    	            && booking.getCheckOutDate().isAfter(existingBooking.getCheckInDate()))
		    {
	    		  return "Property is already booked for these dates";	    		   
		    }
	    }
		
	    
	    if(booking.getCheckInDate().compareTo(
	            booking.getCheckOutDate()) >= 0)
	    {
	        return "Invalid Booking Dates";
	    }

	    long days = booking.getCheckOutDate().toEpochDay()
	            - booking.getCheckInDate().toEpochDay();

	    int totalPrice = (int)days * property.getPricePerNight();

	    booking.setTotalPrice(totalPrice);

	    booking.setUser(user);

	    booking.setProperty(property);
	    
	    booking.setBookingStatus(BookingStatus.PENDING);

	    bookRepo.save(booking);

	    return "Booking Created Successfully";
	}


	@Override
	public String cancelBooking(Long id) {
		Booking booking = bookRepo.findById(id).orElse(null);

	    if (booking == null) {
	        return "Booking Not Found";
	    }

	    if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
	        return "Booking Already Cancelled";
	    }

	    booking.setBookingStatus(BookingStatus.CANCELLED);

	    bookRepo.save(booking);

	    return "Booking Cancelled Successfully";
	}


	@Override
	public List<Booking> viewMyBookings(Long userId) {
		User user = userRepo.findById(userId).orElse(null);

	    if (user == null) {
	        return null;
	    }

	    List<Booking> bookingList = bookRepo.findByUser(user);

	    return bookingList;
	}
	
	@Override
	public Booking getBookingById(Long id) {
	    return bookRepo.findById(id).orElse(null);
	}

}
