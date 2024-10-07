package com.ktpm1.restaurant.services.impls;

import com.ktpm1.restaurant.dtos.request.FoodRequest;
import com.ktpm1.restaurant.models.FileUpload;
import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.SessionTime;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        food.setSessionTime(foodRequest.getSessionTime());

        MultipartFile file = foodRequest.getFile();
        if (file != null && !file.isEmpty()) {
            try {
                FileUpload fileUpload = fileUploadService.uploadFile(file);
                food.setImageCode(fileUpload.getFileCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<MultipartFile> files = foodRequest.getFiles();
        if (files != null) {
            List<String> imageList = new ArrayList<>();
            try {
                for (MultipartFile multipartFile : files) {
                    FileUpload fileUpload = fileUploadService.uploadFile(multipartFile);
                    imageList.add(fileUpload.getFileCode());
                }
                food.setImageList(imageList);
            } catch (Exception e) {
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
                    food1.setSessionTime(foodRequest.getSessionTime());

                    MultipartFile file = foodRequest.getFile();

                    Food updatedFood = null;

                    if (file != null && !file.isEmpty()) {
                        String fileCode = food1.getImageCode();
                        try {
                            FileUpload fileUpload = fileUploadService.uploadFile(file);
                            food1.setImageCode(fileUpload.getFileCode());
                            fileUploadService.deleteFileByFileCode(fileCode);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        List<MultipartFile> files = foodRequest.getFiles();
                        if (files != null) {
                            List<String> imageListOfFood = food1.getImageList();
                            List<String> imageList = new ArrayList<>();
                            try {
                                for (MultipartFile multipartFile : files) {
                                    FileUpload fileUpload = fileUploadService.uploadFile(multipartFile);
                                    imageList.add(fileUpload.getFileCode());
                                }
                                food1.setImageList(imageList);
                                updatedFood = foodRepository.save(food1);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            for(String imageCode : imageListOfFood) {
                                try {
                                    fileUploadService.deleteFileByFileCode(imageCode);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
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

    @Override
    public List<Food> getAllFoods(String keyword) {
        return foodRepository.getAllFood(keyword);
    }

    @Override
    public List<Food> getAllFoodsBySessionTime() {
        LocalTime now = LocalTime.now();

        if (now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(10, 0))) {
            return foodRepository.findAllBySessionTime(SessionTime.MORNING);
        } else if (now.isAfter(LocalTime.of(10, 0)) && now.isBefore(LocalTime.of(14, 0))) {
            return foodRepository.findAllBySessionTime(SessionTime.MORNING);
        } else if (now.isAfter(LocalTime.of(14, 0)) && now.isBefore(LocalTime.of(18, 0))) {
            return foodRepository.findAllBySessionTime(SessionTime.EVENING);
        } else if (now.isAfter(LocalTime.of(18, 0)) && now.isBefore(LocalTime.of(22, 0))) {
            return foodRepository.findAllBySessionTime(SessionTime.NIGHT);
        }
        return foodRepository.findAllBySessionTime(SessionTime.MORNING);
    }
}
