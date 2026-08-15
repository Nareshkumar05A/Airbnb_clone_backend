package com.airbnb.dto;

import lombok.Data;

@Data
public class PropertyDto {

    private String propertyName;

    private String description;

    private String address;

    private String city;

    private String state;

    private String country;

    private int pricePerNight;

    private int maxGuests;

    private int bedrooms;

    private int bathrooms;

    private boolean available;

}