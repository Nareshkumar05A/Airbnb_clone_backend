package com.airbnb.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.airbnb.entity.Property;
import com.airbnb.entity.User;
import com.airbnb.entity.Wishlist;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.UserRepository;
import com.airbnb.repository.WishlishRepository;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlishRepository wishRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PropertyRepository propertyRepo;

    @InjectMocks
    private WishlistServiceImpl wishlistService;

    private User user;
    private Property property;
    private Wishlist wishlist;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setuId(1L);
        user.setuName("Naresh123");

        property = new Property();
        property.setPropertyId(10L);
        property.setPropertyName("Beach House");

        wishlist = new Wishlist();
        wishlist.setWishlistId(100L);
        wishlist.setUser(user);
        wishlist.setProperty(property);
    }

    @Test
    void addWishlist_success() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(wishRepo.existsByUserAndProperty(user, property))
                .thenReturn(false);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("Wishlist Added Successfully", result);

        assertEquals(user, wishlist.getUser());
        assertEquals(property, wishlist.getProperty());

        verify(wishRepo).save(wishlist);
    }

    @Test
    void addWishlist_dataMissing() {

        String result =
                wishlistService.addWishlist(null);

        assertEquals("Wishlist data is missing", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_userMissing() {

        wishlist.setUser(null);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("User data is missing", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_userIdMissing() {

        user.setuId(null);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("User Id is missing", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_propertyMissing() {

        wishlist.setProperty(null);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("Property data is missing", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_propertyIdMissing() {

        property.setPropertyId(null);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("Property Id is missing", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("User Not Found", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_propertyNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.empty());

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals("Property Not Found", result);

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void addWishlist_alreadyExists() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepo.findById(10L))
                .thenReturn(Optional.of(property));

        when(wishRepo.existsByUserAndProperty(user, property))
                .thenReturn(true);

        String result =
                wishlistService.addWishlist(wishlist);

        assertEquals(
                "Property already added to wishlist",
                result
        );

        verify(wishRepo, never()).save(any(Wishlist.class));
    }

    @Test
    void viewWishlist_success() {

        List<Wishlist> wishlists =
                Arrays.asList(wishlist);

        when(userRepo.findById(1L))
                .thenReturn(Optional.of(user));

        when(wishRepo.findByUser(user))
                .thenReturn(wishlists);

        List<Wishlist> result =
                wishlistService.viewWishlist(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(wishlist, result.get(0));

        verify(wishRepo).findByUser(user);
    }

    @Test
    void viewWishlist_userNotFound() {

        when(userRepo.findById(1L))
                .thenReturn(Optional.empty());

        List<Wishlist> result =
                wishlistService.viewWishlist(1L);

        assertNull(result);

        verify(wishRepo, never())
                .findByUser(any(User.class));
    }

    @Test
    void removeWishlist_success() {

        when(wishRepo.findById(100L))
                .thenReturn(Optional.of(wishlist));

        String result =
                wishlistService.removeWishlist(100L);

        assertEquals(
                "Wishlist Removed Successfully",
                result
        );

        verify(wishRepo).delete(wishlist);
    }

    @Test
    void removeWishlist_notFound() {

        when(wishRepo.findById(100L))
                .thenReturn(Optional.empty());

        String result =
                wishlistService.removeWishlist(100L);

        assertEquals(
                "Wishlist Not Found",
                result
        );

        verify(wishRepo, never())
                .delete(any(Wishlist.class));
    }
}