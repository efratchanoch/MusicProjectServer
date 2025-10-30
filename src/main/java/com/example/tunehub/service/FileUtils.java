package com.example.tunehub.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileUtils {
    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\media\\";

    private static String IMAGES_FOLDER = "\\images\\";
    private static String AUDIO_FOLDER = "\\audio\\";
    private static String VIDEO_FOLDER = "\\video\\";
    private static String DOCUMENTS_FOLDER = "\\documents\\";

    // Images
    public static void uploadImage(MultipartFile file) throws IOException {
        String path = UPLOAD_DIRECTORY + IMAGES_FOLDER + file.getOriginalFilename();
        Path fileName = Paths.get(path);
        Files.write(fileName, file.getBytes());
    }

    public static String getImage(String path) throws IOException {
        Path fileName = Paths.get(UPLOAD_DIRECTORY + IMAGES_FOLDER + path);
        byte[] byteImage = Files.readAllBytes(fileName);
        return Base64.getEncoder().encodeToString(byteImage);
    }
   //--------------------------Audio--------------------
    public static void uploadAudio(MultipartFile file) throws IOException, InterruptedException {
        String originalPath = UPLOAD_DIRECTORY + AUDIO_FOLDER + file.getOriginalFilename();
        File dest = new File(originalPath);
        file.transferTo(dest); // שמירה ראשונית

        String outputPath = UPLOAD_DIRECTORY + AUDIO_FOLDER + "encoded_" + file.getOriginalFilename();

        // קידוד ל-AAC באמצעות FFmpeg
        String command = String.format(
                "ffmpeg -i \"%s\" -c:a aac -b:a 128k \"%s\"",
                originalPath, outputPath
        );
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }


    // ------------------ Video ------------------
    public static void uploadVideo(MultipartFile file) throws IOException, InterruptedException {
        String originalPath = UPLOAD_DIRECTORY + VIDEO_FOLDER + file.getOriginalFilename();
        File dest = new File(originalPath);
        file.transferTo(dest); // שמירה ראשונית

        String outputPath = UPLOAD_DIRECTORY + VIDEO_FOLDER + "encoded_" + file.getOriginalFilename();

        // קידוד ל-H.264 + AAC באמצעות FFmpeg
        String command = String.format(
                "ffmpeg -i \"%s\" -c:v libx264 -preset slow -crf 22 -c:a aac \"%s\"",
                originalPath, outputPath
        );
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
    }

    // ------------------ Documents (לשלמות) ------------------
    public static void uploadDocument(MultipartFile file) throws IOException {
        String path = UPLOAD_DIRECTORY + DOCUMENTS_FOLDER + file.getOriginalFilename();
        Path fileName = Paths.get(path);
        Files.write(fileName, file.getBytes());
    }
}


