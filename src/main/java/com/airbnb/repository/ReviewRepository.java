package com.airbnb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

    List<Review> findByProperty(Property property);

    boolean existsByUserAndProperty(User user, Property property);

    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.property = :property")
    void deleteByProperty(@Param("property") Property property);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Review r WHERE r.user = :user")
    void deleteByUser(@Param("user") User user);

}