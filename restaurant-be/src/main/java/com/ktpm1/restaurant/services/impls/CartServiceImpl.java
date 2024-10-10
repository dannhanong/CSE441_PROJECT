package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.CartRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.CartItem;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.User;
import com.ktpm1.restaurant.repositories.CartItemRepository;
import com.ktpm1.restaurant.repositories.CartRepository;
import com.ktpm1.restaurant.repositories.FoodRepository;
import com.ktpm1.restaurant.repositories.UserRepository;
import com.ktpm1.restaurant.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Cart getCartByUser(String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public ResponseMessage addToCart(String username, CartRequest cartRequest) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            cart = cartRepository.findByUser(user);
        }

        Food food = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
        if (food != null) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getFood().getId().equals(cartRequest.getFoodId()))
                    .findFirst()
                    .orElse(null);
            if (cartItem != null) {
                cartItem.setQuantity(cartRequest.getQuantity());
                cartItem.setPrice(food.getPrice() * cartItem.getQuantity());
                cartItemRepository.save(cartItem);
                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
                cartRepository.save(cart);
                return new ResponseMessage(200, "Thêm vào giỏ hàng thành công");
            } else {
                cartItem = CartItem.builder()
                        .cart(cart)
                        .food(food)
                        .quantity(cartRequest.getQuantity())
                        .price(food.getPrice() * cartRequest.getQuantity())
                        .build();
                cart.getCartItems().add(cartItem);
                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
                cartRepository.save(cart);

                return new ResponseMessage(200, "Thêm vào giỏ hàng thành công");
            }
        }
        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

    @Override
    public ResponseMessage addToCart(String username, Long foodId) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
            cart = cartRepository.findByUser(user);
        }

        Food food = foodRepository.findById(foodId).orElse(null);
        if (food != null) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getFood().getId().equals(foodId))
                    .findFirst()
                    .orElse(null);
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItem.setPrice(food.getPrice() * cartItem.getQuantity());
                long totalPrice = cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum();
                cart.setTotalPrice(totalPrice);
                cartItemRepository.save(cartItem);
                cartRepository.save(cart);
                return new ResponseMessage(200, "Thêm vào giỏ hàng thành công");
            } else {
                cartItem = CartItem.builder()
                        .cart(cart)
                        .food(food)
                        .quantity(1)
                        .price(food.getPrice())
                        .build();
                cart.getCartItems().add(cartItem);
                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
                cartRepository.save(cart);
                return new ResponseMessage(200, "Thêm vào giỏ hàng thành công");
            }
        }
        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

    @Override
    public ResponseMessage updateCart(String username, CartRequest cartRequest) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        Food food = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
        if (food != null) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getFood().getId().equals(cartRequest.getFoodId()))
                    .findFirst()
                    .orElse(null);
            if (cartItem != null) {
                cartItem.setQuantity(cartRequest.getQuantity());
                cartItem.setPrice(cartRequest.getQuantity() * food.getPrice());
                cartItemRepository.save(cartItem);
                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
                cartRepository.save(cart);
                return new ResponseMessage(200, "Cập nhật giỏ hàng thành công");
            }
        }
        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

    @Override
    @Transactional
    public ResponseMessage removeFromCart(String username, Long foodId) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        Food food = foodRepository.findById(foodId).orElse(null);
        if (food != null) {
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getFood().getId().equals(foodId))
                    .findFirst()
                    .orElse(null);
            if (cartItem != null) {
                cart.getCartItems().remove(cartItem);
                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
                cartRepository.save(cart);

                return new ResponseMessage(200, "Xóa khỏi giỏ hàng thành công");
            }
        }

        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

    @Override
    @Transactional
    public ResponseMessage clearCart(String username) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return new ResponseMessage(200, "Xóa giỏ hàng thành công");
    }
}
