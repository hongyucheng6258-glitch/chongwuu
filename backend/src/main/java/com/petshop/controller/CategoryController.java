package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.entity.Category;
import com.petshop.security.CurrentUser;
import com.petshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ApiResponse<List<Category>> list() {
        return ApiResponse.success(categoryService.list());
    }

    @PostMapping
    public ApiResponse<Category> create(@RequestBody Category category) {
        checkAdmin();
        return ApiResponse.success("创建成功", categoryService.create(category));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @RequestBody Category category) {
        checkAdmin();
        return ApiResponse.success("更新成功", categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        checkAdmin();
        categoryService.delete(id);
        return ApiResponse.success("删除成功", null);
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new com.petshop.common.BusinessException(403, "需要管理员权限");
        }
    }
}
