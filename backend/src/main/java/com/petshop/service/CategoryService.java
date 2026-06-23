package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.entity.Category;
import com.petshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> list() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category create(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new BusinessException("分类名称已存在");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("分类不存在"));
        existing.setName(category.getName());
        existing.setIcon(category.getIcon());
        return categoryRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
