package com.example.tunehub.service;

import java.io.ByteArrayInputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Base64;
import java.util.UUID;

public class FileUtils {
    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\media";
    private static String IMAGES_FOLDER = "images";
    private static String AUDIO_FOLDER = "audio";
    private static String VIDEO_FOLDER = "video";
    private static String DOCUMENTS_FOLDER = "docs";

    // Genric

    /**
     * יוצר שם קובץ ייחודי באמצעות UUID ושומר על הסיומת המקורית.
     */
    public static String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        // 1. קביעת הסיומת (Extension)
        String fileExtension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");

        if (lastDotIndex != -1) {
            // אם נמצאה נקודה, קח את החלק שאחריה (הסיומת)
            fileExtension = originalFileName.substring(lastDotIndex);
        }

        // 2. יצירת UUID ושילובו עם הסיומת
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        return uniqueFileName;
    }

    // Images
    public static void uploadImage(MultipartFile file, String uniqueFileName) throws IOException {
        Path fileName = Paths.get(UPLOAD_DIRECTORY, IMAGES_FOLDER, uniqueFileName);
        Files.write(fileName, file.getBytes());
    }

    //מקבלת את שם התמונה + סיומת
    // מחזירה את התמונה בביס 64 של הנתיב המלא
    public static String imageToBase64(String path) {

        Path fileName;
        if (path.contains(":") || path.startsWith("/") || path.startsWith("\\")) {
            fileName = Paths.get(path);
        } else {
            try {
                Path baseDir = Paths.get(UPLOAD_DIRECTORY, IMAGES_FOLDER);
                fileName = baseDir.resolve(path);
            } catch (InvalidPathException e) {
                return null;
            }
        }
        try {
            byte[] byteImage = Files.readAllBytes(fileName);
            return Base64.getEncoder().encodeToString(byteImage);
        } catch (IOException e) {
            return null;
        }
    }

    //--------------------------Audio--------------------
    public static void uploadAudio(MultipartFile file) throws IOException, InterruptedException {
        String originalPath = UPLOAD_DIRECTORY + AUDIO_FOLDER + file.getOriginalFilename();
        File dest = new File(originalPath);
        file.transferTo(dest); // שמירה ראשונית

        String outputPath = UPLOAD_DIRECTORY + AUDIO_FOLDER + "encoded_" + file.getOriginalFilename();

        try {
            FFmpeg ffmpeg = new FFmpeg(); // ה-binary מטופל ע"י ה-dependency
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(originalPath)
                    .addOutput(outputPath)
                    .setAudioCodec("aac")
                    .setAudioBitRate(128000)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
            executor.createJob(builder).run();

            System.out.println("אודיו מקודד ושמור ב: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("שגיאה בהמרת האודיו עם FFmpeg", e);
        }

    }

    public static InputStreamResource getAudio(String path) throws IOException {
        Path filename = Paths.get(UPLOAD_DIRECTORY + AUDIO_FOLDER + path);
        InputStream stream = Files.newInputStream(filename);
        return new InputStreamResource(stream);
    }

    // ------------------ Video ------------------
    public static void uploadVideo(MultipartFile file) throws IOException {
        String originalPath = UPLOAD_DIRECTORY + VIDEO_FOLDER + file.getOriginalFilename();
        File dest = new File(originalPath);
        file.transferTo(dest); // שמירה ראשונית

        String outputPath = UPLOAD_DIRECTORY + VIDEO_FOLDER + "encoded_" + file.getOriginalFilename();

        try {
            FFmpeg ffmpeg = new FFmpeg(); // ה-binary מטופל ע"י ה-dependency
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(originalPath)
                    .addOutput(outputPath)
                    .setVideoCodec("libx264")
                    .setVideoPreset("slow")
                    .setConstantRateFactor(22)
                    .setAudioCodec("aac")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
            executor.createJob(builder).run();

            System.out.println("וידאו מקודד ושמור ב: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("שגיאה בהמרת הווידאו עם FFmpeg", e);
        }
    }

    // Documents
    public static void uploadDocument(MultipartFile file, String uniqueFileName) throws IOException {
        Path fileName = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, uniqueFileName);

        Files.write(fileName, file.getBytes());


    }

    public static int getPDFpageCount(byte[] pdfBytes) {

        if (pdfBytes == null || pdfBytes.length == 0) {
            return 0;
        }
        // שימוש ב-try-with-resources כדי להבטיח סגירת PDDocument
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            System.err.println("שגיאה בניתוח קובץ PDF לחישוב עמודים: " + e.getMessage());
            return 0;
        }
    }


    public static String docsToBase64(String uniqueFileName) {

        if (uniqueFileName == null || uniqueFileName.isEmpty()) {
            return null;
        }

        try {
            // 1. בנה את הנתיב הסופי
            Path fileNamePath = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, uniqueFileName);

            // 2. קרא את הביתים (פעולה שיכולה לזרוק IOException)
            byte[] fileBytes = Files.readAllBytes(fileNamePath);

            // 3. קודד ל-Base64 והחזר
            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (IOException e) {
            // טיפול בשגיאה: אם הקובץ לא נמצא, אין הרשאה, או שגיאת I/O אחרת
            System.err.println("שגיאה בקידוד קובץ ל-Base64: " + uniqueFileName + ". הודעה: " + e.getMessage());
            e.printStackTrace();

            // 4. החזרת null במקרה של כישלון קריאה
            return null;
        }
    }


}


