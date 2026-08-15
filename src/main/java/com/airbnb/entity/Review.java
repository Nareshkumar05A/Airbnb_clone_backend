package com.airbnb.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDate reviewDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

}