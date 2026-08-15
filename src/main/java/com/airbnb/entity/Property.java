package com.airbnb.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "property")
@Data
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @Column(nullable = false)
    private String propertyName;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private int pricePerNight;

    @Column(nullable = false)
    private int maxGuests;

    @Column(nullable = false)
    private int bedrooms;

    @Column(nullable = false)
    private int bathrooms;

    @Column(nullable = false)
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}