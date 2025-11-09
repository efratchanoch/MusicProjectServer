package com.example.tunehub.service;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Base64;

public class FileUtils {
    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\media";
    private static String IMAGES_FOLDER = "images";
    private static String AUDIO_FOLDER = "audio";
    private static String VIDEO_FOLDER = "video";
    private static String DOCUMENTS_FOLDER = "documents";

    // Images
    public static void uploadImage(MultipartFile file) throws IOException {
        String path = UPLOAD_DIRECTORY + IMAGES_FOLDER + file.getOriginalFilename();
        Path fileName = Paths.get(path);
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
    public static void uploadDocument(MultipartFile file) throws IOException {
        Path fileName = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, file.getOriginalFilename());
        Files.write(fileName, file.getBytes());
    }

    // FileUtils.java (פונקציה מעודכנת)
    public static void saveBase64ToFile(String base64Content, String fileName) throws IOException {
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Content);

        // השימוש בנתיב הקבוע + השם המקורי השמור
        Path filePath = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, fileName);

        Files.write(filePath, decodedBytes);
    }
}


