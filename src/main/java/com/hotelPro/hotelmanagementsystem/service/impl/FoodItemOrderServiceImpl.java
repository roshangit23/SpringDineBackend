package com.hotelPro.hotelmanagementsystem.service.impl;

import com.hotelPro.hotelmanagementsystem.model.FoodItemOrder;
import com.hotelPro.hotelmanagementsystem.repository.FoodItemOrderRepository;
import com.hotelPro.hotelmanagementsystem.service.FoodItemOrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodItemOrderServiceImpl implements FoodItemOrderService {

    @Autowired
    private FoodItemOrderRepository foodItemOrderRepository;

    @Override
    @Transactional
    public void saveFoodItemOrder(FoodItemOrder foodItemOrder) {
        foodItemOrderRepository.save(foodItemOrder);
    }
}
