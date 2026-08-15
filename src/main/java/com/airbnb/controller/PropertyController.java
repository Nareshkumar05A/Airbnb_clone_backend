package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airbnb.dto.PropertyDto;
import com.airbnb.entity.Property;
import com.airbnb.service.PropertyService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/property")
public class PropertyController {

	@Autowired
	private PropertyService proServ;
	
	@PostMapping("/add")
	public String addProperty(@RequestBody Property property)
	{
		String result = proServ.addProperty(property);
		return result;
	}
	
	@GetMapping("/all")
	public List<Property> getAll()
	{
	    return proServ.fetchAllProperty();
	}
	
	@GetMapping("/get/{id}")
	public Property getById(@PathVariable Long id)
	{
		Property result = proServ.getById(id);
		return result;
	}
	
	@PutMapping("/update/{id}")
	public String updateId(@PathVariable Long id, @RequestBody PropertyDto propertyDto)
	{
	     String result = proServ.updateById(id, propertyDto);
	     return result;
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteId(@PathVariable Long id)
	{
		String result = proServ.deleteById(id);
		return result;
	}
	
	@GetMapping("/search/city/{city}")
	public List<Property> searchByCity(@PathVariable String city){

	    return proServ.searchByCity(city);
	}
	
	@GetMapping("/search/state/{state}")
	public List<Property> searchByState(@PathVariable String state){

	    return proServ.searchByState(state);
	}
	
	@GetMapping("/search/country/{country}")
	public List<Property> searchByCountry(@PathVariable String country){

	    return proServ.searchByCountry(country);
	}
	
	@GetMapping("/search/price/{price}")
	public List<Property> searchByPrice(@PathVariable int price){

	    return proServ.searchByPrice(price);
	}
	
	@GetMapping("/search/guest/{guest}")
	public List<Property> searchByGuest(@PathVariable int guest){

	    return proServ.searchByGuest(guest);
	}
	
	@GetMapping("/host/{userId}")
	public List<Property> hostProperties(@PathVariable Long userId) {

	    return proServ.getHostProperties(userId);
	}
	
	
	@GetMapping("/search/available")
	public List<Property> searchAvailable(){

	    return proServ.searchAvailable();
	}
}
