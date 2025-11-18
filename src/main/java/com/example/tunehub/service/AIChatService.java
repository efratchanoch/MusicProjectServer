package com.example.tunehub.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIChatService {
    private final ChatClient chatClient;
       private final static String SYSYEM_INSTRUCTION= """
            """;
    public AIChatService(ChatClient.Builder chatClient) {
       this.chatClient = chatClient.build();
    }

    public String getResponse(String prompt){
        SystemMessage systemMessage=new SystemMessage(SYSYEM_INSTRUCTION);
        UserMessage userMessage=new UserMessage(prompt);

        List<Message> messageList= List.of(systemMessage,userMessage);

        return chatClient.prompt().messages(messageList).call().content();
    }
}

