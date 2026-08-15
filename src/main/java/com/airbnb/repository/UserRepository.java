package com.airbnb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.airbnb.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	 @Query("SELECT u FROM User u WHERE u.uName = :uName")
	 User findByUName(String uName);
	 
	 boolean existsByUName(String uName);

	 boolean existsByEmail(String email);
}
