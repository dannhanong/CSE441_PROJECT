package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.dtos.request.CartRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.User;

public interface CartService {
    Cart getCartByUser(String username);
    ResponseMessage addToCart(String username, CartRequest cartRequest);
    ResponseMessage addToCart(String username, Long foodId);
    ResponseMessage updateCart(String username, CartRequest cartRequest);
    ResponseMessage removeFromCart(String username, Long foodId);
    ResponseMessage clearCart(String username);
}
