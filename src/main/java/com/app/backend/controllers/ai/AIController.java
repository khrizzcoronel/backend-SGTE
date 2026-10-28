package com.app.backend.controllers.ai;

import com.app.backend.dtos.ai.request.ChatRequest;
import com.app.backend.dtos.ai.response.ChatResponse;
import com.app.backend.services.ai.AIChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AIController {

    private final AIChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ChatResponse.builder().success(false).error("El mensaje no puede estar vacío.").build());
        }
        return ResponseEntity.ok(chatService.chat(request));
    }
}