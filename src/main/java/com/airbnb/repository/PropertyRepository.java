package com.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByCity(String city);

  
    List<Property> findByCountry(String country);

    List<Property> findByState(String state);

    
    List<Property> findByPricePerNightBetween(int minPrice, int maxPrice);

    
    List<Property> findByMaxGuestsGreaterThanEqual(int guests);

    List<Property> findByAvailable(boolean available);


	List<Property> findByPricePerNightLessThanEqual(int price);
	
	@Query("SELECT p FROM Property p WHERE p.user.uId = :uId")
	List<Property> findByUserUId(@Param("uId") Long uId);

	List<Property> findByUser(User user);

    int countByUser(User user);

    int countByUserAndAvailable(User user, boolean available);

}