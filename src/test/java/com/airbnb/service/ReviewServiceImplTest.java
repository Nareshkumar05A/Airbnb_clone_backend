package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.airbnb.entity.Property;
import com.airbnb.entity.Review;
import com.airbnb.entity.User;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PropertyRepository propertyRepo;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private User user;
    private Property property;
    private Review review;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setuId(1L);
        user.setuName("Naresh123");

        property = new Property();
        property.setPropertyId(10L);
        property.setPropertyName("Beach House");

        review = new Review();
        review.setReviewId(100L);
        review.setRating(5);
        review.setComment("Excellent property");
        review.setUser(user);
        review.setProperty(property);
    }

    @Test
    void addReview_success() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(reviewRepo.existsByUserAndProperty(user, property))
                .thenReturn(false);

        String result = reviewService.addReview(review);

        assertEquals("Review Added Successfully", result);

        assertEquals(user, review.getUser());
        assertEquals(property, review.getProperty());
        assertEquals(LocalDate.now(), review.getReviewDate());

        verify(reviewRepo).save(review);
    }

    @Test
    void addReview_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        String result = reviewService.addReview(review);

        assertEquals("User Not Found", result);

        verify(reviewRepo, never()).save(any(Review.class));
    }

    @Test
    void addReview_propertyNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.empty());

        String result = reviewService.addReview(review);

        assertEquals("Property Not Found", result);

        verify(reviewRepo, never()).save(any(Review.class));
    }

    @Test
    void addReview_ratingBelowOne() {

        review.setRating(0);

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        String result = reviewService.addReview(review);

        assertEquals("Rating should be between 1 and 5", result);

        verify(reviewRepo, never()).save(any(Review.class));
    }

    @Test
    void addReview_ratingAboveFive() {

        review.setRating(6);

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        String result = reviewService.addReview(review);

        assertEquals("Rating should be between 1 and 5", result);

        verify(reviewRepo, never()).save(any(Review.class));
    }

    @Test
    void addReview_alreadyReviewed() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(reviewRepo.existsByUserAndProperty(user, property))
                .thenReturn(true);

        String result = reviewService.addReview(review);

        assertEquals("You already reviewed this property", result);

        verify(reviewRepo, never()).save(any(Review.class));
    }

    @Test
    void getPropertyReviews_success() {

        List<Review> reviews = Arrays.asList(review);

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(reviewRepo.findByProperty(property))
                .thenReturn(reviews);

        List<Review> result =
                reviewService.getPropertyReviews(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(review, result.get(0));
    }

    @Test
    void getPropertyReviews_propertyNotFound() {

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.empty());

        List<Review> result =
                reviewService.getPropertyReviews(10L);

        assertNull(result);

        verify(reviewRepo, never())
                .findByProperty(any(Property.class));
    }

    @Test
    void updateReview_success() {

        Review updatedReview = new Review();

        updatedReview.setRating(4);
        updatedReview.setComment("Good property");

        when(reviewRepo.findById(100L))
                .thenReturn(Optional.of(review));

        String result =
                reviewService.updateReview(100L, updatedReview);

        assertEquals("Review Updated Successfully", result);

        assertEquals(4, review.getRating());
        assertEquals("Good property", review.getComment());

        verify(reviewRepo).save(review);
    }

    @Test
    void updateReview_notFound() {

        Review updatedReview = new Review();

        updatedReview.setRating(4);
        updatedReview.setComment("Good property");

        when(reviewRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                reviewService.updateReview(100L, updatedReview);

        assertEquals("Review Not Found", result);

        verify(reviewRepo, never())
                .save(any(Review.class));
    }

    @Test
    void deleteReview_success() {

        when(reviewRepo.findById(100L))
                .thenReturn(Optional.of(review));

        String result =
                reviewService.deleteReview(100L);

        assertEquals("Review Deleted Successfully", result);

        verify(reviewRepo).delete(review);
    }

    @Test
    void deleteReview_notFound() {

        when(reviewRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                reviewService.deleteReview(100L);

        assertEquals("Review Not Found", result);

        verify(reviewRepo, never())
                .delete(any(Review.class));
    }

    @Test
    void averageRating_success() {

        Review review2 = new Review();
        review2.setRating(3);

        Review review3 = new Review();
        review3.setRating(4);

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(reviewRepo.findByProperty(property))
                .thenReturn(Arrays.asList(
                        review,
                        review2,
                        review3
                ));

        double result =
                reviewService.averageRating(10L);

        assertEquals(4.0, result);
    }

    @Test
    void averageRating_propertyNotFound() {

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.empty());

        double result =
                reviewService.averageRating(10L);

        assertEquals(0, result);

        verify(reviewRepo, never())
                .findByProperty(any(Property.class));
    }

    @Test
    void averageRating_noReviews() {

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(reviewRepo.findByProperty(property))
                .thenReturn(Collections.emptyList());

        double result =
                reviewService.averageRating(10L);

        assertEquals(0, result);
    }
}