package com.example.tunehub.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import org.springframework.ai.tool.annotation.Tool;
import java.util.regex.Pattern;


@Component
public class MusicMetadataSearchTool {

    private static final Logger logger = Logger.getLogger(MusicMetadataSearchTool.class.getName());
    private final RestTemplate restTemplate = new RestTemplate();

    // הזרקת הערכים מקובץ ה-properties
    @Value("${google.search.api-key}")
    private String apiKey;

    @Value("${google.search.cx}")
    private String cxId;

    /**
     * פונקציה זו מחפשת מידע על המלחין וכותב המילים באמצעות Google Custom Search.
     * * @param title כותרת היצירה (Transliterated) מה-PDF.
     * @return מחרוזת JSON עם השדות foundComposer ו-foundLyricist.
     */

    @Tool // ✅ המיקום הנכון!
    public String searchForMetadata(String title) {


        // יצירת שאילתת החיפוש:
        // אנו מחפשים "מלחין" ו"כותב מילים" עבור כותרת היצירה.
        String query = title + " sheet music composer and lyricist";

        // בניית ה-URL לקריאת Google Custom Search API
        String url = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/customsearch/v1")
                .queryParam("key", apiKey) // מפתח API
                .queryParam("cx", cxId)   // מזהה מנוע החיפוש
                .queryParam("q", query)   // השאילתה
                .queryParam("num", 3)     // בקש 3 תוצאות מובילות
                .toUriString();

        try {
            // 💡 קריאת HTTP אמיתית ל-API של גוגל
            String jsonResponse = restTemplate.getForObject(url, String.class);
            logger.info("External search performed for: " + title);

            // 🚨 ניתוח התוצאות
            // ניתוח אוטומטי של API מורכב דורש מחלקות DTO מורכבות.
            // לצורך דוגמה לא סטטית ופשוטה, ננסה לחלץ מידע גולמי.

            String composer = extractComposerFromGoogleResults(jsonResponse);
            String lyricist = extractLyricistFromGoogleResults(jsonResponse);

            // החזרת הנתונים למודל ה-AI
            return String.format(
                    """
                    {
                      "foundComposer": "%s",
                      "foundLyricist": "%s"
                    }
                    """,
                    composer.isEmpty() ? "" : escapeJson(composer),
                    lyricist.isEmpty() ? "No Lyricist Found" : escapeJson(lyricist)
            );

        } catch (Exception e) {
            logger.severe("Error during external search: " + e.getMessage());
            // במקרה של כשל בחיפוש, החזר תוצאות ריקות
            return """
                   {
                     "foundComposer": "",
                     "foundLyricist": ""
                   }
                   """;
        }
    }

    // --- פונקציות עזר לניתוח הנתונים (מבוססות על ניסיון בדרך כלל) ---
    // פונקציות אלו דורשות כוונון עדין בהתאם למה שגוגל מחזירה
    private String extractComposerFromGoogleResults(String response) {
        // דוגמה פשוטה: חיפוש כותרות ותיאורים של תוצאות החיפוש
        // מימוש אמיתי ידרוש ניתוח JSON עמוק יותר (Jackson/ObjectMapper)

        // כאן נשתמש בביטוי רגולרי גס לדוגמה:
        Pattern pattern = Pattern.compile("(Composer|Arranger|By|מלחין):\\s*([^,.\"]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return "";
    }

    private String extractLyricistFromGoogleResults(String response) {
        Pattern pattern = Pattern.compile("(Lyricist|Words|מילים):\\s*([^,.\"]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(2).trim();
        }
        return "";
    }

    // פונקציה פשוטה להימנע מבעיות ב-JSON
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"");
    }
}