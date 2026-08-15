package com.airbnb.dto;

import lombok.Data;

@Data
public class DashboardDto {

    private int totalProperties;

    private int totalBookings;

    private int availableProperties;

    private int totalEarnings;

}