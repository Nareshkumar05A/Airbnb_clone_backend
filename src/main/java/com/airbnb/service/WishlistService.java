package com.airbnb.service;

import java.util.List;

import com.airbnb.entity.Wishlist;

public interface WishlistService {

	String addWishlist(Wishlist wishlist);

	List<Wishlist> viewWishlist(Long userId);

	String removeWishlist(Long wishlistId);

}
