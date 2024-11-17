package com.ked.core.services;

import com.ked.core.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> findAllByUserIdToChoose(Long userId);

    boolean exists(Long categoryId);

    void setName(String name, Long userId);

    void setType(String type, Long userId);

    void setCreatedAt(Long userId);
}
