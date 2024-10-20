package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.CartRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.User;

public interface CartService {
    Cart getCartByUser(String username);
    ResponseMessage addToCart(String username, CartRequest cartRequest);
    ResponseMessage updateCart(String username, Long cartItemId, int quantity);
    ResponseMessage removeFromCart(String username, Long cartItemId);
    ResponseMessage clearCart(String username);
}
