package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.common.BusinessException;
import com.petshop.dto.VideoRequest;
import com.petshop.entity.Video;
import com.petshop.security.CurrentUser;
import com.petshop.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /** 前台 - 视频列表 */
    @GetMapping("/list")
    public ApiResponse<Page<Video>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ApiResponse.success(videoService.list(page, size));
    }

    /** 前台 - 视频详情（含播放量+1） */
    @GetMapping("/detail/{id}")
    public ApiResponse<Video> detail(@PathVariable Long id) {
        return ApiResponse.success(videoService.detail(id));
    }

    /** 前台 - 按商品获取视频 */
    @GetMapping("/product/{productId}")
    public ApiResponse<List<Video>> byProduct(@PathVariable Long productId) {
        return ApiResponse.success(videoService.listByProduct(productId));
    }

    /** 前台 - 按分类获取视频 */
    @GetMapping("/category/{category}")
    public ApiResponse<List<Video>> byCategory(@PathVariable String category) {
        return ApiResponse.success(videoService.listByCategory(category));
    }

    /** 后台 - CRUD */
    @PostMapping
    public ApiResponse<Video> create(@RequestBody VideoRequest req) {
        checkAdmin();
        return ApiResponse.success("视频上传成功", videoService.create(req));
    }

    @PutMapping("/{id}")
    public ApiResponse<Video> update(@PathVariable Long id, @RequestBody VideoRequest req) {
        checkAdmin();
        return ApiResponse.success("视频更新成功", videoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        checkAdmin();
        videoService.delete(id);
        return ApiResponse.success("视频已删除", null);
    }

    private void checkAdmin() {
        if (!CurrentUser.isAdmin()) {
            throw new BusinessException(403, "需要管理员权限");
        }
    }
}
