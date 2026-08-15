package com.airbnb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Override
    public String addReview(Review review) {

        User user = userRepo.findById(
                review.getUser().getuId()).orElse(null);

        if(user == null)
            return "User Not Found";

        Property property = propertyRepo.findById(
                review.getProperty().getPropertyId()).orElse(null);

        if(property == null)
            return "Property Not Found";

        if(review.getRating() < 1 || review.getRating() > 5)
            return "Rating should be between 1 and 5";

        if(reviewRepo.existsByUserAndProperty(user, property))
            return "You already reviewed this property";

        review.setUser(user);
        review.setProperty(property);
        review.setReviewDate(LocalDate.now());

        reviewRepo.save(review);

        return "Review Added Successfully";
    }

    @Override
    public List<Review> getPropertyReviews(Long propertyId) {

        Property property = propertyRepo.findById(propertyId).orElse(null);

        if(property == null)
            return null;

        return reviewRepo.findByProperty(property);
    }

    @Override
    public String updateReview(Long reviewId, Review review) {

        Review oldReview = reviewRepo.findById(reviewId).orElse(null);

        if(oldReview == null)
            return "Review Not Found";

        if(review.getRating() >=1 && review.getRating()<=5)
            oldReview.setRating(review.getRating());

        oldReview.setComment(review.getComment());

        reviewRepo.save(oldReview);

        return "Review Updated Successfully";
    }

    @Override
    public String deleteReview(Long reviewId) {

        Review review = reviewRepo.findById(reviewId).orElse(null);

        if(review == null)
            return "Review Not Found";

        reviewRepo.delete(review);

        return "Review Deleted Successfully";
    }

    @Override
    public double averageRating(Long propertyId) {

        Property property = propertyRepo.findById(propertyId).orElse(null);

        if(property == null)
            return 0;

        List<Review> reviews = reviewRepo.findByProperty(property);

        if(reviews.size() == 0)
            return 0;

        double sum = 0;

        for(Review review : reviews)
        {
            sum += review.getRating();
        }

        return sum / reviews.size();
    }

}