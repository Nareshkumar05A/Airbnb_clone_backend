package com.airbnb.service;

import java.util.List;

import com.airbnb.entity.Booking;

public interface BookingService {

	String createBook(Booking book);

	String cancelBooking(Long id);

	List<Booking> viewMyBookings(Long userId);
	
	Booking getBookingById(Long id);


}
