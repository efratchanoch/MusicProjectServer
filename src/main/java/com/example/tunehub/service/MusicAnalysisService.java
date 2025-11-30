package com.example.tunehub.service;

import org.springframework.stereotype.Service;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicAnalysisService {

    /**
     * מקבל קובץ MusicXML ומחזיר ניתוח: כלי נגינה, סולם ורמת קושי
     */
    public AnalysisResult analyzeMusicXML(File mxlFile) throws Exception {
        List<String> instruments = new ArrayList<>();
        String keySignature = "Unknown";
        String difficulty = "Unknown";

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(mxlFile);
        doc.getDocumentElement().normalize();

        // 1. קבלת סולם
        NodeList keyList = doc.getElementsByTagName("key");
        if (keyList.getLength() > 0) {
            Element key = (Element) keyList.item(0);
            Node fifthsNode = key.getElementsByTagName("fifths").item(0);
            if (fifthsNode != null) {
                int fifths = Integer.parseInt(fifthsNode.getTextContent());
                keySignature = mapFifthsToKey(fifths);
            }
        }

        // 2. זיהוי כלי נגינה
        NodeList partList = doc.getElementsByTagName("score-part");
        for (int i = 0; i < partList.getLength(); i++) {
            Element part = (Element) partList.item(i);
            String partName = part.getElementsByTagName("part-name").item(0).getTextContent();
            instruments.add(partName);
        }

        // 3. הערכת רמת קושי (פשטות: מספר התווים, אקורדים מורכבים)
        NodeList noteList = doc.getElementsByTagName("note");
        int noteCount = noteList.getLength();
        if (noteCount < 50) difficulty = "Easy";
        else if (noteCount < 150) difficulty = "Medium";
        else difficulty = "Hard";

        return new AnalysisResult(instruments, keySignature, difficulty);
    }

    /**
     * ממפה את מספר החמישיות בסולם לסולם קריא
     */
    private String mapFifthsToKey(int fifths) {
        switch(fifths) {
            case 0: return "C major / A minor";
            case 1: return "G major / E minor";
            case 2: return "D major / B minor";
            case 3: return "A major / F# minor";
            case 4: return "E major / C# minor";
            case 5: return "B major / G# minor";
            case 6: return "F# major / D# minor";
            case 7: return "C# major / A# minor";
            case -1: return "F major / D minor";
            case -2: return "Bb major / G minor";
            case -3: return "Eb major / C minor";
            case -4: return "Ab major / F minor";
            case -5: return "Db major / Bb minor";
            case -6: return "Gb major / Eb minor";
            case -7: return "Cb major / Ab minor";
            default: return "Unknown";
        }
    }

    /**
     * תוצאה של הניתוח
     */
    public static class AnalysisResult {
        public final List<String> instruments;
        public final String keySignature;
        public final String difficulty;

        public AnalysisResult(List<String> instruments, String keySignature, String difficulty) {
            this.instruments = instruments;
            this.keySignature = keySignature;
            this.difficulty = difficulty;
        }
    }


}
