package com.petshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiChatRequest {
    /** 用户消息 */
    private String message;
    /** 可选：上下文类型 (product_recommend/pet_care/order_query/general) */
    private String contextType;
    /** 可选：上下文ID */
    private Long contextId;
    /** 多轮对话历史 */
    private List<ChatMessage> history;
    /** 是否使用流式输出 */
    private boolean stream = false;

    @Data
    public static class ChatMessage {
        private String role;   // user / assistant
        private String content;
    }
}
