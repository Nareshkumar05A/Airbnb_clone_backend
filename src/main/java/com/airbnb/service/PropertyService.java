package com.airbnb.service;

import java.util.List;

import com.airbnb.dto.PropertyDto;
import com.airbnb.entity.Property;

public interface PropertyService {

    String addProperty(Property property);

    List<Property> fetchAllProperty();

    Property getById(Long id);

    String updateById(Long id, PropertyDto propertyDto);

    String deleteById(Long id);

    List<Property> getByCity(String city);

    List<Property> getByCountry(String country);

    List<Property> getByState(String state);

    List<Property> getByPriceRange(int minPrice, int maxPrice);
    
    List<Property> getByGuests(int guests);

	List<Property> searchByCity(String city);

	List<Property> searchByState(String state);

	List<Property> searchByCountry(String country);

	List<Property> searchByPrice(int price);

	List<Property> searchByGuest(int guest);

	List<Property> searchAvailable();
	
	List<Property> getHostProperties(Long userId);

}