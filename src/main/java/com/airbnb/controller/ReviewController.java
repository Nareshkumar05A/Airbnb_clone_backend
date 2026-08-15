package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.airbnb.entity.Review;
import com.airbnb.service.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewServ;

    @PostMapping("/add")
    public String addReview(@RequestBody Review review){

        return reviewServ.addReview(review);
    }

    @GetMapping("/property/{propertyId}")
    public List<Review> getReviews(@PathVariable Long propertyId){

        return reviewServ.getPropertyReviews(propertyId);
    }

    @PutMapping("/update/{reviewId}")
    public String updateReview(@PathVariable Long reviewId,
                               @RequestBody Review review){

        return reviewServ.updateReview(reviewId, review);
    }

    @DeleteMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId){

        return reviewServ.deleteReview(reviewId);
    }

    @GetMapping("/rating/{propertyId}")
    public double averageRating(@PathVariable Long propertyId){

        return reviewServ.averageRating(propertyId);
    }

}