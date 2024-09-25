package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.FoodRequest;
import com.ktpm1.restaurant.models.FileUpload;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.repositories.CategoryRepository;
import com.ktpm1.restaurant.repositories.FoodRepository;
import com.ktpm1.restaurant.services.FileUploadService;
import com.ktpm1.restaurant.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public Food createFood(FoodRequest foodRequest) {
        Food food = new Food();
        food.setName(foodRequest.getName());
        food.setDescription(foodRequest.getDescription());
        food.setPrice(foodRequest.getPrice());
        food.setCategory(categoryRepository.findById(foodRequest.getCategoryId()).orElse(null));

        MultipartFile file = foodRequest.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                FileUpload fileUpload = fileUploadService.uploadFile(file);
                food.setImage(fileUpload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return foodRepository.save(food);
    }

    @Override
    public Food updateFood(FoodRequest foodRequest, Long id) {
        return foodRepository.findById(id).map(
                food1 -> {
                    food1.setName(foodRequest.getName());
                    food1.setDescription(foodRequest.getDescription());
                    food1.setPrice(foodRequest.getPrice());
                    food1.setCategory(categoryRepository.findById(foodRequest.getCategoryId()).orElse(null));

                    MultipartFile file = foodRequest.getFile();

                    Food updatedFood = null;

                    if (file != null && !file.isEmpty()) {
                        String fileCode = food1.getImage().getFileCode();
                        try {
                            FileUpload fileUpload = fileUploadService.uploadFile(file);
                            food1.setImage(fileUpload);
                            updatedFood = foodRepository.save(food1);
                            fileUploadService.deleteFileByFileCode(fileCode);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return updatedFood;
                }
        ).orElse(null);
    }

    @Override
    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }

    @Override
    public Food getFood(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Food> getAllFoods(Pageable pageable, String keyword) {
        return foodRepository.getAllFood(pageable, keyword);
    }
}
