package com.airbnb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.dto.PropertyDto;
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

import jakarta.transaction.Transactional;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private WishlishRepository wishRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Override
    public String addProperty(Property property) {
    	
    	if (property.getPropertyName() == null ||
    		    property.getPropertyName().isBlank()) {
    		    return "Property Name is required";
    		}

        if (property.getPricePerNight() <= 0)
            return "Price should be greater than 0";

        if (property.getMaxGuests() <= 0)
            return "Guests should be greater than 0";

        if (property.getBedrooms() <= 0)
            return "Bedrooms should be greater than 0";

        if (property.getBathrooms() <= 0)
            return "Bathrooms should be greater than 0";

        if(property.getUser()==null)
            return "Host Required";
        
        
        User host = userRepo.findById(property.getUser().getuId())
                .orElse(null);
        
        if(host==null)
            return "Host Not Found";

        property.setUser(host);

        propertyRepo.save(property);

        return "Property Added Successfully";
    }

    @Override
    public List<Property> fetchAllProperty() {

        return propertyRepo.findAll();
    }

    @Override
    public Property getById(Long id) {

        return propertyRepo.findById(id).orElse(null);
    }

    @Override
    public String updateById(Long id, PropertyDto dto) {

        Property property = propertyRepo.findById(id).orElse(null);

        if (property == null) 
        {
            return "Property Not Found";
        }

        property.setPropertyName(dto.getPropertyName());
        property.setDescription(dto.getDescription());
        property.setAddress(dto.getAddress());
        property.setCity(dto.getCity());
        property.setState(dto.getState());
        property.setCountry(dto.getCountry());
        property.setPricePerNight(dto.getPricePerNight());
        property.setMaxGuests(dto.getMaxGuests());
        property.setBedrooms(dto.getBedrooms());
        property.setBathrooms(dto.getBathrooms());
        property.setAvailable(dto.isAvailable());

        propertyRepo.save(property);

        return "Property Updated Successfully";
    }

    @Transactional
    @Override
    public String deleteById(Long id) {

        Property property = propertyRepo.findById(id).orElse(null);

        if (property == null)
            return "Property Not Found";

        List<Booking> bookings = bookingRepo.findByProperty(property);

        if (!bookings.isEmpty()) {
            List<Payment> payments = paymentRepo.findByBookingIn(bookings);
            paymentRepo.deleteAll(payments);
            bookingRepo.deleteAll(bookings);
        }

        List<Wishlist> wishlists = wishRepo.findByProperty(property);
        wishRepo.deleteAll(wishlists);

        List<Review> reviews = reviewRepo.findByProperty(property);
        reviewRepo.deleteAll(reviews);

        propertyRepo.delete(property);

        return "Property Deleted Successfully";
    }
    @Override
    public List<Property> getByCity(String city) {

        return propertyRepo.findByCity(city);
    }

    @Override
    public List<Property> getByCountry(String country) {

        return propertyRepo.findByCountry(country);
    }

    @Override
    public List<Property> getByState(String state) {

        return propertyRepo.findByState(state);
    }

    @Override
    public List<Property> getByPriceRange(int minPrice, int maxPrice) {

        return propertyRepo.findByPricePerNightBetween(minPrice, maxPrice);
    }

    @Override
    public List<Property> getByGuests(int guests) {

        return propertyRepo.findByMaxGuestsGreaterThanEqual(guests);
    }

	@Override
	public List<Property> searchByCity(String city) {
		return propertyRepo.findByCity(city);
	}

	@Override
	public List<Property> searchByState(String state) {
		return propertyRepo.findByState(state);
	}

	@Override
	public List<Property> searchByCountry(String country) {
		return propertyRepo.findByCountry(country);
	}

	@Override
	public List<Property> searchByPrice(int price) {
		return propertyRepo.findByPricePerNightLessThanEqual(price);
	}

	@Override
	public List<Property> searchByGuest(int guest) {
		return propertyRepo.findByMaxGuestsGreaterThanEqual(guest);
	}

	@Override
	public List<Property> searchAvailable() {
		return propertyRepo.findByAvailable(true);
	}

	@Override
	public List<Property> getHostProperties(Long userId) {
		return propertyRepo.findByUserUId(userId);
	}

}