package com.example.tunehub.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
       private final static String SYSYEM_INSTRUCTION= """
               אתה עוזר וירטואלי באתר TuneHub — פלטפורמה לשיתוף מוזיקה ווידאו בין מוזיקאים. האתר כולל:
                                    - ספריית פוסטים עם תוכן מוזיקלי (וידאו, אודיו, טקסטים, תמונות).
                                    - אפשרות להעלות קבצי מוזיקה ווידאו.
                                    - מערכת תגובות והערות לפוסטים.
                                    - אפשרות לשתף כלי נגינה וליצור קהילה מוזיקלית.
                                    - חיפוש ותיוג תוכן לפי ז'אנרים, מוזיקאים וכלי נגינה.
               
                                    תפקידך:
                                    1. לענות על שאלות המשתמש בנוגע לכל הפיצ'רים של האתר.
                                    2. לתת הסברים קצרים, ברורים ומדויקים, עם דוגמאות.
                                    3. להדריך את המשתמש כיצד לבצע פעולות: להעלות פוסטים, לשתף קבצים, לכתוב תגובות, לשתף כלי נגינה.
                                    4. לא להמציא מידע מחוץ למה שקיים באתר.
                                    5. לשמור על טון מקצועי, מנומס וידידותי.
                                    6. אם השאלה לא קשורה ל‑TuneHub — להסביר בעדינות שאתה עוזר רק בנושאי האתר.
               
                                    דוגמאות למענה:
                                    - משתמש שואל על העלאת פוסט → הסבר שלב-שלב איך להעלות פוסט עם קובץ אודיו/וידאו.
                                    - משתמש שואל על תגובות → הסבר איך לצפות, להוסיף ולנהל תגובות.
                                    - משתמש שואל על כלי נגינה → הסבר איך לשתף ולהציג אותם בפרופיל או בפוסט 
                                    תענה על הכל בנעימות וסבלנות, תהיה חכם ותשמח את כולם ותחמיא להם .   
                                            """;

    public AIChatService(ChatClient.Builder chatClient, ChatMemory chatMemory) {
       this.chatClient = chatClient.build();
       this.chatMemory=chatMemory;
    }

//    public String getResponse(String prompt,String conversationId){
//        List<Message> messageList= new ArrayList<>();
//        //ההנחיה הראשונה
//        messageList.add(new SystemMessage(SYSYEM_INSTRUCTION));
//        SystemMessage systemMessage=new SystemMessage(SYSYEM_INSTRUCTION);
//        UserMessage userMessage=new UserMessage(prompt);
//
//        List<Message> messageList= List.of(systemMessage,userMessage);
//
//        return chatClient.prompt().messages(messageList).call().content();
//    }

    public String getResponse(String prompt,String conversationId){
        List<Message> messageList= new ArrayList<>();
        //ההנחיה הראשונה
        messageList.add(new SystemMessage(SYSYEM_INSTRUCTION));
        //מוסיף את כל ההודעות ששיכות לשיחה
        messageList.addAll(chatMemory.get(conversationId));
        //השאלה הנוכחית
        UserMessage userMessage=new UserMessage(prompt);
        messageList.add(userMessage);

        String aiResponse=chatClient.prompt().messages(messageList).call().content();

        //שמירת התגובה בזיכרון
        //התגובה של AI
        AssistantMessage aiMessage=new AssistantMessage(aiResponse);
        List<Message> messageList1=List.of(userMessage,aiMessage);
        //מוסיפים לזיכרון את השאלה והתשובה
        chatMemory.add(conversationId,messageList1);

        return  aiResponse;
//        SystemMessage systemMessage=new SystemMessage(SYSYEM_INSTRUCTION);
//        UserMessage userMessage=new UserMessage(prompt);
//
//        List<Message> messageList= List.of(systemMessage,userMessage);
//
//        return chatClient.prompt().messages(messageList).call().content();
    }
}

