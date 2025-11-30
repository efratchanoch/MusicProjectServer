package com.example.tunehub.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PdfToMusicXmlService {


    private final String audiverisPath = "C:\\Audiveris\\bin\\Audiveris.bat"; // או הנתיב שלך ל-Audiveris

    /**
     * מקבל PDF bytes ומחזיר את תוכן MusicXML
     */
    public String convertPdfToMusicXml(byte[] pdfBytes) throws IOException, InterruptedException {
        // 1️⃣ שמירה זמנית של PDF
        Path tempPdf = Files.createTempFile("sheet_", ".pdf");
        Files.write(tempPdf, pdfBytes);

        // 2️⃣ תיקיית פלט זמנית
        Path outputDir = Files.createTempDirectory("audiveris_output");

        // 3️⃣ פקודת הרצה של Audiveris
        ProcessBuilder pb = new ProcessBuilder(
                audiverisPath,
                "-batch",          // הרצה אוטומטית בלי GUI
                "-export",         // יצוא ל-MusicXML
                "-output", outputDir.toString(),
                tempPdf.toString()
        );

        pb.redirectErrorStream(true); // לשים את הסטנדרט + שגיאות יחד
        Process process = pb.start();

        // קריאה לפלט להמתנה
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // רישום לפלט קונסולה, יכול להסיר
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Audiveris failed with exit code " + exitCode);
        }

        // 4️⃣ קריאת קובץ MusicXML שנוצר
        // הנחה: Audiveris שומר אותו עם אותו שם PDF אבל סיומת .mxl
        String musicXmlFileName = tempPdf.getFileName().toString().replace(".pdf", ".mxl");
        Path musicXmlPath = outputDir.resolve(musicXmlFileName);

        if (!Files.exists(musicXmlPath)) {
            throw new RuntimeException("MusicXML file not found: " + musicXmlPath);
        }

        String musicXmlContent = Files.readString(musicXmlPath);

        // 5️⃣ מחיקת קבצים זמניים
        Files.deleteIfExists(tempPdf);
        // אפשר למחוק גם את כל outputDir אם רוצים

        return musicXmlContent;
    }


}
