package com.airbnb.dto;

import lombok.Data;

@Data
public class AdminDashboardDto {

    private int totalUsers;

    private int totalProperties;

    private int totalBookings;

    private int totalPayments;

    private int totalRevenue;

}