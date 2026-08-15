package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @Mock
    private WishlishRepository wishRepo;

    @Mock
    private ReviewRepository reviewRepo;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private User user;
    private Property property;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setuId(1L);
        user.setuName("Naresh123");
        user.setEmail("naresh@gmail.com");

        property = new Property();
        
        property.setPropertyId(10L); 

        property.setPropertyName("Beach House");
        property.setDescription("Beautiful beach house");
        property.setAddress("Beach Road");
        property.setCity("Chennai");
        property.setState("Tamil Nadu");
        property.setCountry("India");

        property.setPricePerNight(2000);
        property.setMaxGuests(4);
        property.setBedrooms(2);
        property.setBathrooms(2);
        property.setAvailable(true);

        property.setUser(user);
    }

    @Test
    void addProperty_success() {

        when(userRepo.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        String result = propertyService.addProperty(property);

        assertEquals("Property Added Successfully", result);

        verify(userRepo).findById(1L);
        verify(propertyRepo).save(property);
    }

    @Test
    void addProperty_invalidPropertyName() {

        property.setPropertyName("");

        String result = propertyService.addProperty(property);

        assertEquals("Property Name is required", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_invalidPrice() {

        property.setPricePerNight(0);

        String result = propertyService.addProperty(property);

        assertEquals("Price should be greater than 0", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_invalidGuests() {

        property.setMaxGuests(0);

        String result = propertyService.addProperty(property);

        assertEquals("Guests should be greater than 0", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_invalidBedrooms() {

        property.setBedrooms(0);

        String result = propertyService.addProperty(property);

        assertEquals("Bedrooms should be greater than 0", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_invalidBathrooms() {

        property.setBathrooms(0);

        String result = propertyService.addProperty(property);

        assertEquals("Bathrooms should be greater than 0", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_hostMissing() {

        property.setUser(null);

        String result = propertyService.addProperty(property);

        assertEquals("Host Required", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void addProperty_hostNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(java.util.Optional.empty());

        String result = propertyService.addProperty(property);

        assertEquals("Host Not Found", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void fetchAllProperty_success() {

        List<Property> properties =
                Arrays.asList(property);

        when(propertyRepo.findAll())
                .thenReturn(properties);

        List<Property> result =
                propertyService.fetchAllProperty();

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));

        verify(propertyRepo).findAll();
    }

    @Test
    void getById_success() {

        when(propertyRepo.findById(10L))
                .thenReturn(java.util.Optional.of(property));

        Property result =
                propertyService.getById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getPropertyId());
    }

    @Test
    void getById_notFound() {

        when(propertyRepo.findById(10L))
                .thenReturn(java.util.Optional.empty());

        Property result =
                propertyService.getById(10L);

        assertNull(result);
    }

    @Test
    void updateById_success() {

        PropertyDto dto = new PropertyDto();

        dto.setPropertyName("Updated House");
        dto.setDescription("Updated Description");
        dto.setAddress("Updated Address");
        dto.setCity("Bangalore");
        dto.setState("Karnataka");
        dto.setCountry("India");
        dto.setPricePerNight(5000);
        dto.setMaxGuests(6);
        dto.setBedrooms(3);
        dto.setBathrooms(3);
        dto.setAvailable(true);

        when(propertyRepo.findById(10L))
                .thenReturn(java.util.Optional.of(property));

        String result =
                propertyService.updateById(10L, dto);

        assertEquals("Property Updated Successfully", result);

        assertEquals("Updated House",
                property.getPropertyName());

        assertEquals(5000,
                property.getPricePerNight());

        verify(propertyRepo).save(property);
    }

    @Test
    void updateById_notFound() {

        PropertyDto dto = new PropertyDto();

        when(propertyRepo.findById(10L))
                .thenReturn(java.util.Optional.empty());

        String result =
                propertyService.updateById(10L, dto);

        assertEquals("Property Not Found", result);

        verify(propertyRepo, never()).save(any(Property.class));
    }

    @Test
    void getByCity_success() {

        when(propertyRepo.findByCity("Chennai"))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getByCity("Chennai");

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
    }

    @Test
    void getByState_success() {

        when(propertyRepo.findByState("Tamil Nadu"))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getByState("Tamil Nadu");

        assertEquals(1, result.size());
    }

    @Test
    void getByCountry_success() {

        when(propertyRepo.findByCountry("India"))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getByCountry("India");

        assertEquals(1, result.size());
    }

    @Test
    void getByPriceRange_success() {

        when(propertyRepo.findByPricePerNightBetween(1000, 5000))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getByPriceRange(1000, 5000);

        assertEquals(1, result.size());
    }

    @Test
    void getByGuests_success() {

        when(propertyRepo.findByMaxGuestsGreaterThanEqual(4))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getByGuests(4);

        assertEquals(1, result.size());
    }

    @Test
    void searchAvailable_success() {

        when(propertyRepo.findByAvailable(true))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.searchAvailable();

        assertEquals(1, result.size());
    }

    @Test
    void getHostProperties_success() {

        when(propertyRepo.findByUserUId(1L))
                .thenReturn(Arrays.asList(property));

        List<Property> result =
                propertyService.getHostProperties(1L);

        assertEquals(1, result.size());
        assertEquals(property, result.get(0));
    }
}