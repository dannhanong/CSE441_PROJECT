package com.ktpm1.restaurant.controllers;

import com.ktpm1.restaurant.dtos.request.CartRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.security.jwt.JwtService;
import com.ktpm1.restaurant.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addToCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(cartService.addToCart(username, cartRequest));
    }

    @GetMapping("/view")
    public Cart viewCart(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String user = jwtService.extractUsername(token);
        return cartService.getCartByUser(user);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseMessage> updateCart(HttpServletRequest request, @RequestBody CartRequest cartRequest) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(cartService.updateCart(username, cartRequest));
    }

    @DeleteMapping("/remove/{foodId}")
    public ResponseEntity<ResponseMessage> removeFromCart(HttpServletRequest request, @PathVariable Long foodId) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(cartService.removeFromCart(username, foodId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ResponseMessage> clearCart(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = jwtService.extractUsername(token);
        return ResponseEntity.ok(cartService.clearCart(username));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("JWT Token is missing");
    }
}
