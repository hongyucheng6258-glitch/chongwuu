package com.petshop.controller;

import com.petshop.common.ApiResponse;
import com.petshop.dto.AiChatRequest;
import com.petshop.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/chat")
    public ApiResponse<Map<String, Object>> chat(@RequestBody AiChatRequest req) {
        return ApiResponse.success(aiService.chat(req));
    }
}
