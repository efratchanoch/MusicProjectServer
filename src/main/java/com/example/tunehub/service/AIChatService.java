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
   // private final AgentTools agentTools;
    //    private final static String SYSYEM_INSTRUCTION= """
//            אתה עוזר AI שנועד לעזור בעניני בישולים ומתכונים בלבד.
//            .ענה בשפה פשוטה שגם משתמשים ללא ניסיון בבישול יוכלו להבין אותך
//           אם יש מוצר חלופי שאמור להתאים למתכון תתן את כל החלופות.
//           אם מישהו שואל אותך על מזג אוויר תענה לו גם.
//           אם שואלים שאלות שלא קשורות לעניני מתכונים ובישול ענה בנימוס שאתה עוזר רק לעניני בישול.
//            """;
    private final static String SYSYEM_INSTRUCTION="אתה עוזר AI נחמד מאוד.";
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

