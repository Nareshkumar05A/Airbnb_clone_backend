package com.airbnb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "wishlist")
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishlistId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;
}