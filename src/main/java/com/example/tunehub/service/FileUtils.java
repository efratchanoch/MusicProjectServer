package com.example.tunehub.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
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

    // ------------------ Documents (לשלמות) ------------------
    public static void uploadDocument(MultipartFile file) throws IOException {
        String path = UPLOAD_DIRECTORY + DOCUMENTS_FOLDER + file.getOriginalFilename();
        Path fileName = Paths.get(path);
        Files.write(fileName, file.getBytes());
    }
}


