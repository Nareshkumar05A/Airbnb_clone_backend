package com.airbnb.service;

import java.util.List;

import com.airbnb.entity.Review;

public interface ReviewService {

    String addReview(Review review);

    List<Review> getPropertyReviews(Long propertyId);

    String updateReview(Long reviewId, Review review);

    String deleteReview(Long reviewId);

    double averageRating(Long propertyId);

}