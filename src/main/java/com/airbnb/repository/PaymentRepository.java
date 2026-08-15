package com.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airbnb.entity.Booking;
import com.airbnb.entity.Payment;

import jakarta.transaction.Transactional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

    boolean existsByBooking(Booking booking);

    Payment findByBooking(Booking booking);

    @Query("SELECT p FROM Payment p LEFT JOIN p.booking b LEFT JOIN b.user u WHERE u.uId = :userId")
	List<Payment> findByBookingUserUId(Long userId);
    @Query("SELECT p FROM Payment p WHERE p.booking.property.user.uId = :userId")
    List<Payment> findByBookingPropertyUserUId(Long uId);
    
    List<Payment> findByBookingIn(List<Booking> bookings);

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.booking IN :bookings")
    void deleteByBookingIn(@Param("bookings") List<Booking> bookings);
    
}