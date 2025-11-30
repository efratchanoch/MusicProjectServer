package com.example.tunehub.service;


import com.example.tunehub.model.EDifficultyLevel;
import com.example.tunehub.model.EScale;
import org.springframework.stereotype.Service;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfMusicAnalyzer {


    private final MusicAnalysisService analysisService;

    public PdfMusicAnalyzer(MusicAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * מקבל PDF כ-Bytes, ממיר ל-MusicXML בזיכרון ומחזיר את התוצאה המנותחת
     */
    public MusicAnalysisService.AnalysisResult analyzePdf(byte[] pdfBytes) throws Exception {
        // 1. שמירת PDF בזיכרון זמני
        File tempPdf = File.createTempFile("temp_score", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempPdf)) {
            fos.write(pdfBytes);
        }

        // 2. המרה ל-MusicXML בעזרת Audiveris (Batch mode)
        File tempMxl = File.createTempFile("temp_score", ".mxl");

        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp", "C:\\Program Files\\Audiveris\\app\\audiveris.jar;C:\\Program Files\\Audiveris\\app\\*",
                "Audiveris",
                "-batch",
                "-export",
                tempPdf.getAbsolutePath()
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // כאן אפשר ללוג את ההתקדמות אם רוצים
            }
        }
        proc.waitFor();

        // 3. חיפוש אוטומטי של קובץ ה-MusicXML שנוצר
        File generatedMxl = new File(tempPdf.getParent(), tempPdf.getName().replace(".pdf", ".mxl"));
        if (!generatedMxl.exists()) {
            throw new RuntimeException("MusicXML conversion failed");
        }

        // 4. ניתוח הקובץ בעזרת MusicAnalysisService
        MusicAnalysisService.AnalysisResult result = analysisService.analyzeMusicXML(generatedMxl);

        // 5. מחיקת הקבצים הזמניים
        tempPdf.delete();
        generatedMxl.delete();

        return result;
    }


}
