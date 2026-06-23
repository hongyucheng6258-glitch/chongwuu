package com.petshop.service;

import com.petshop.common.BusinessException;
import com.petshop.dto.VideoRequest;
import com.petshop.entity.Video;
import com.petshop.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public Page<Video> list(int page, int size) {
        return videoRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }

    public Video detail(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("视频不存在"));
        // 增加播放量
        video.setViewCount(video.getViewCount() + 1);
        return videoRepository.save(video);
    }

    public List<Video> listByProduct(Long productId) {
        return videoRepository.findByProductId(productId);
    }

    public List<Video> listByCategory(String category) {
        return videoRepository.findByCategory(category);
    }

    @Transactional
    public Video create(VideoRequest req) {
        Video video = new Video();
        applyRequest(video, req);
        return videoRepository.save(video);
    }

    @Transactional
    public Video update(Long id, VideoRequest req) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("视频不存在"));
        applyRequest(video, req);
        return videoRepository.save(video);
    }

    @Transactional
    public void delete(Long id) {
        videoRepository.deleteById(id);
    }

    private void applyRequest(Video video, VideoRequest req) {
        if (req.getTitle() != null) video.setTitle(req.getTitle());
        if (req.getDescription() != null) video.setDescription(req.getDescription());
        if (req.getVideoUrl() != null) video.setVideoUrl(req.getVideoUrl());
        if (req.getCoverUrl() != null) video.setCoverUrl(req.getCoverUrl());
        if (req.getProductId() != null) video.setProductId(req.getProductId());
        if (req.getCategory() != null) video.setCategory(req.getCategory());
    }
}
