package com.airbnb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.WishlishRepository;

@Service
public class WishlistServiceImpl implements WishlistService {

	@Autowired
    private WishlishRepository wishRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PropertyRepository propertyRepo;

    @Override
    public String addWishlist(Wishlist wishlist) {

        if (wishlist == null) {
            return "Wishlist data is missing";
        }

        if (wishlist.getUser() == null) {
            return "User data is missing";
        }

        if (wishlist.getUser().getuId() == null) {
            return "User Id is missing";
        }

        if (wishlist.getProperty() == null) {
            return "Property data is missing";
        }

        if (wishlist.getProperty().getPropertyId() == null) {
            return "Property Id is missing";
        }

        User user = userRepo.findById(wishlist.getUser().getuId()).orElse(null);

        if (user == null) {
            return "User Not Found";
        }

        Property property = propertyRepo.findById(
                wishlist.getProperty().getPropertyId()).orElse(null);

        if (property == null) {
            return "Property Not Found";
        }

        if (wishRepo.existsByUserAndProperty(user, property)) {
            return "Property already added to wishlist";
        }

        wishlist.setUser(user);
        wishlist.setProperty(property);

        wishRepo.save(wishlist);

        return "Wishlist Added Successfully";
    }

    @Override
    public List<Wishlist> viewWishlist(Long userId) {

        User user = userRepo.findById(userId).orElse(null);

        if(user == null)
            return null;

        return wishRepo.findByUser(user);
    }

    @Override
    public String removeWishlist(Long wishlistId) {

        Wishlist wishlist = wishRepo.findById(wishlistId).orElse(null);

        if(wishlist == null)
            return "Wishlist Not Found";

        wishRepo.delete(wishlist);

        return "Wishlist Removed Successfully";
    }


}
