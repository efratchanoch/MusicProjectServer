package com.example.tunehub.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileDownloadController {

    private final Path fileStorageLocation = Paths.get("./uploads/scores")
            .toAbsolutePath().normalize();

    /**
     * מטפל בבקשת GET להורדת קובץ (למשל, תווי נגינה).
     *
     * @param fileName שם הקובץ המלא לבצע הורדה (למשל, "my_score.pdf")
     * @param request  אובייקט בקשת ה-HTTP, משמש לזיהוי סוג ה-MIME
     * @return תגובת HTTP עם הקובץ המצורף להורדה
     */
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        // 1. איתור הקובץ במערכת הקבצים
        Resource resource;
        try {
            // בונה את הנתיב המלא לקובץ
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            resource = new UrlResource(filePath.toUri());

            // בדיקה אם הקובץ קיים וניתן לקרוא אותו
            if (!resource.exists() || !resource.isReadable()) {
                // מחזיר 404 Not Found אם הקובץ לא קיים
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException ex) {
            // שגיאה בבניית ה-URL
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 2. קביעת סוג התוכן (Content Type) של הקובץ
        String contentType = null;
        try {
            // מנסה לזהות את סוג ה-MIME באמצעות הסיומת
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // אם הזיהוי נכשל, משתמשים ב-Content Type גנרי
            contentType = "application/octet-stream";
        }

        // 3. החזרת הקובץ כ-ResponseEntity
        return ResponseEntity.ok()
                // הגדרת כותרת Content-Type
                .contentType(MediaType.parseMediaType(contentType))
                // הגדרת כותרת Content-Disposition, המורה לדפדפן להוריד את הקובץ
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                // גוף התגובה הוא הקובץ (Resource)
                .body(resource);
    }
}
