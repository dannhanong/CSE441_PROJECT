package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.CartRequest;
import com.ktpm1.restaurant.dtos.response.ResponseMessage;
import com.ktpm1.restaurant.models.*;
import com.ktpm1.restaurant.repositories.*;
import com.ktpm1.restaurant.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private FoodOptionRepository foodOptionRepository;

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
        if (user == null) {
            return new ResponseMessage(400, "Người dùng không tồn tại");
        }

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        Food food = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
        if (food == null) {
            return new ResponseMessage(400, "Món ăn không tồn tại");
        }

        List<FoodOption> selectedOptions;
        if (cartRequest.getFoodOptionIds() != null && !cartRequest.getFoodOptionIds().isEmpty()) {
            selectedOptions = foodOptionRepository.findAllById(cartRequest.getFoodOptionIds());
        } else {
            selectedOptions = new ArrayList<>();
        }

        int totalPrice = food.getPrice();
        for (FoodOption option : selectedOptions) {
            totalPrice += option.getPrice();
        }

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getFood().getId().equals(cartRequest.getFoodId())
                        && item.getOptions().containsAll(selectedOptions)
                        && selectedOptions.containsAll(item.getOptions()) // Kiểm tra option trùng khớp
                        && item.getOptions().size() == selectedOptions.size()) // Kiểm tra kích thước của danh sách option
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequest.getQuantity());
            existingCartItem.setPrice(existingCartItem.getQuantity() * totalPrice);
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .food(food)
                    .quantity(cartRequest.getQuantity())
                    .price(totalPrice * cartRequest.getQuantity())
                    .options(selectedOptions)
                    .build();
            cart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        cart.setTotalPrice(cart.getCartItems().stream().mapToInt(CartItem::getPrice).sum());
        cartRepository.save(cart);

        return new ResponseMessage(200, "Thêm vào giỏ hàng thành công");
    }

    @Override
    @Transactional
    public ResponseMessage removeFromCart(String username, Long cartItemId) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            cart.getCartItems().remove(cartItem);
            cart.setTotalPrice(cart.getCartItems().stream().mapToInt(CartItem::getPrice).sum());
            cartRepository.save(cart);

            return new ResponseMessage(200, "Xóa khỏi giỏ hàng thành công");
        }

        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

    @Override
    public ResponseMessage updateCart(String username, Long cartItemId, int quantity) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUser(user);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItem != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getId().equals(cartItemId)) {
                    item.setQuantity(quantity);
                    item.setPrice(item.getFood().getPrice() * quantity);

                    List<FoodOption> selectedOptions = item.getOptions();
                    int totalPrice = item.getFood().getPrice();
                    for (FoodOption option : selectedOptions) {
                        totalPrice += option.getPrice();
                    }
                    item.setPrice(totalPrice * quantity);
                    cartItemRepository.save(item);
                    break;
                }
            }

            cart.setTotalPrice(cart.getCartItems().stream().mapToInt(CartItem::getPrice).sum());
            cartRepository.save(cart);

            return new ResponseMessage(200, "Cập nhật giỏ hàng thành công");
        }

        return new ResponseMessage(400, "Món ăn không tồn tại");
    }

//    @Override
//    public ResponseMessage updateCart(String username, CartRequest cartRequest) {
//        User user = userRepository.findByUsername(username);
//        Cart cart = cartRepository.findByUser(user);
//        Food food = foodRepository.findById(cartRequest.getFoodId()).orElse(null);
//
//        if (food != null) {
//            List<FoodOption> selectedOptions;
//            if (cartRequest.getFoodOptionIds() != null && !cartRequest.getFoodOptionIds().isEmpty()) {
//                selectedOptions = foodOptionRepository.findAllById(cartRequest.getFoodOptionIds());
//            } else {
//                selectedOptions = new ArrayList<>();
//            }
//
//            System.out.println(cartRequest.getFoodId());
//
//            CartItem cartItem = cart.getCartItems().stream()
//                    .filter(item -> item.getFood().getId().equals(cartRequest.getFoodId())) // Kiểm tra các tùy chọn
//                    .findFirst()
//                    .orElse(null);
//
//            if (cartItem != null) {
//                // Cập nhật số lượng và giá dựa trên số lượng và các tùy chọn
//                int totalPrice = food.getPrice();
//                for (FoodOption option : selectedOptions) {
//                    totalPrice += option.getPrice();
//                }
//                cartItem.setQuantity(cartRequest.getQuantity());
//                cartItem.setPrice(totalPrice * cartItem.getQuantity());
//                cartItem.setOptions(selectedOptions); // Cập nhật các tùy chọn
//                cartItemRepository.save(cartItem);
//
//                // Cập nhật tổng giá của giỏ hàng
//                cart.setTotalPrice(cart.getCartItems().stream().mapToLong(CartItem::getPrice).sum());
//                cartRepository.save(cart);
//
//                return new ResponseMessage(200, "Cập nhật giỏ hàng thành công");
//            }
//        }
//        return new ResponseMessage(400, "Món ăn không tồn tại");
//    }

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
