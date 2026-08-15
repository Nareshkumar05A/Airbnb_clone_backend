package com.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;

import jakarta.transaction.Transactional;

public interface WishlishRepository extends JpaRepository<Wishlist, Long> {

	List<Wishlist> findByUser(User user);

    boolean existsByUserAndProperty(User user, Property property);
    
    List<Wishlist> findByProperty(Property property);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.property = :property")
    void deleteByProperty(@Param("property") Property property);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.user = :user")
    void deleteByUser(@Param("user") User user);
    
    
}
