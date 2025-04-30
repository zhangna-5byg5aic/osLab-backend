package com.yupi.springbootinit.controller;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/chat")
public class ChatController {
    static String apiKey = "eea68ae8-c186-4bc6-871f-46a23450f06e";
    static String baseUrl = "https://ark.cn-beijing.volces.com/api/v3";
    static ArkService service = ArkService.builder().baseUrl(baseUrl).apiKey(apiKey).build();
    static String rolePrompt="你是一个擅长计算机课程操作系统的问答助手";

    @PostMapping("/ask")
    public BaseResponse<String> askQuestion(@RequestBody String userQuestion) {
        // 创建消息
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(rolePrompt).build();
        ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userQuestion).build();
        messages.add(systemMessage);
        messages.add(userMessage);

        // 创建请求
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("deepseek-v3-250324") // 使用你实际的模型 ID
                .messages(messages)
                .build();

        // 调用 Ark API 获取回答
        StringBuilder response = new StringBuilder();
        service.createChatCompletion(request).getChoices().forEach(choice -> response.append(choice.getMessage().getContent()));

        return ResultUtils.success(response.toString());
    }
}
