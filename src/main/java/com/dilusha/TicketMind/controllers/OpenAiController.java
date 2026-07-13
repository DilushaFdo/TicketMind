package com.dilusha.TicketMind.controllers;


import com.dilusha.TicketMind.dto.AIRequest;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OpenAiController {

    private OpenAiChatModel chatModel;

    public OpenAiController(OpenAiChatModel chatModel){
        this.chatModel = chatModel;
    }

    @PostMapping("/openai")
    public ResponseEntity<String> getAIAnswer(@RequestBody AIRequest request){
        String response = chatModel.call(request.getMessage());
        return ResponseEntity.ok(response);
    }
}
