package com.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Property;
import com.airbnb.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByProperty(Property property);
	@Query("SELECT b FROM Booking b WHERE b.user = :user")
	List<Booking> findByUser(User user);
	
	@Query("SELECT b FROM Booking b WHERE b.property.user.uId = :uId")
	List<Booking> findByPropertyUserUId(@Param("uId") Long uId);
	
	List<Booking> findByPropertyIn(List<Property> properties);

	@Transactional
	@Modifying
	@Query("DELETE FROM Booking b WHERE b.property = :property")
	void deleteByProperty(@Param("property") Property property);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Booking b WHERE b.user = :user")
	void deleteByUser(@Param("user") User user);
	
}
