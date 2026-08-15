package com.airbnb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.airbnb.entity.Wishlist;
import com.airbnb.service.WishlistService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishServ;

    @PostMapping("/add")
    public String addWishlist(@RequestBody Wishlist wishlist){

        return wishServ.addWishlist(wishlist);
    }

    @GetMapping("/user/{userId}")
    public List<Wishlist> viewWishlist(@PathVariable Long userId){

        return wishServ.viewWishlist(userId);
    }

    @DeleteMapping("/delete/{wishlistId}")
    public String removeWishlist(@PathVariable Long wishlistId){

        return wishServ.removeWishlist(wishlistId);
    }

}