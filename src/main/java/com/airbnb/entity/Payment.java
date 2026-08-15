package com.airbnb.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDate paymentDate;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}