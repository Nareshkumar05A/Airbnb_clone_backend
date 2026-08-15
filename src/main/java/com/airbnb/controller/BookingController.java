package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airbnb.entity.Booking;
import com.airbnb.service.BookingService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService bookServ;
	
	@PostMapping("/new")
	public String NewBook(@RequestBody Booking book)
	{
		String result = bookServ.createBook(book);
		return result;
	}
	
	@PatchMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) 
    {
        return bookServ.cancelBooking(id);
    }
	
	@GetMapping("/user/{userId}")
	public List<Booking> viewMyBookings(@PathVariable Long userId) {

	    return bookServ.viewMyBookings(userId);

	}
	
	@GetMapping("/{id}")
	public Booking getBooking(@PathVariable Long id) {
	    return bookServ.getBookingById(id);
	}
}
