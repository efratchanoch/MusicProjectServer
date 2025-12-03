package com.example.tunehub.controller;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.*;
import com.example.tunehub.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.example.tunehub.service.UsersRatingUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sheetMusic")
public class SheetMusicController {
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final SheetMusicRepository sheetMusicRepository;
    private final SheetMusicMapper sheetMusicMapper;
    private final AuthService authService;
    private final InstrumentRepository instrumentRepository;
    private final SheetMusicCategoryRepository categoryRepository;
    private final SheetAnalysisAgentService agentService;
    private final PdfTextExtractorService extractor;
    private final SheetMusicService sheetMusicService;
    private final LikeRepository likeRepository;
    private final FavoriteRepository favoriteRepository;


    //Get
    @GetMapping("/sheetMusicById/{id}")
    public ResponseEntity<SheetMusicResponseDTO> getSheetMusicById(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            SheetMusic s = sheetMusicRepository.findSheetMusicById(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/sheetsMusic")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusic() {
        try {
         Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAll();
            if (s.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            UsersRatingUtils.calculateAndSetSheetMusicStarRating( s);

            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/sheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByUserId(@PathVariable Long id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUser_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/sheetsMusicByTitle/{title}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByTitle(String title) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllByTitleContainingIgnoreCase(title);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByCategory/{category_id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByCategory(@PathVariable Long category_id) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllByCategories_Id(category_id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByScale/{scale}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByScale(@PathVariable EScale scale) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByScale(scale);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByLevel/{level}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByLevel(@PathVariable EDifficultyLevel level) {
        try {
            Long currentUserId = authService.getCurrentUserId();
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByLevel(level);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s, currentUserId, likeRepository, favoriteRepository), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Delete
    @DeleteMapping("/sheetsMusicByUserId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    public ResponseEntity deleteSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByUserId(id);
            if (s == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            sheetMusicRepository.deleteAllByUserId(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/uploadSheetMusic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SheetMusicResponseDTO> uploadSheetMusic(
            @RequestPart("file") MultipartFile file,
            @RequestPart(name = "image", required = false) MultipartFile image,
            @RequestPart("data") SheetMusicUploadDTO dto) {

        try {
            SheetMusicResponseDTO response = sheetMusicService.upload(dto, file, image);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/documents/{docPath}")
    public ResponseEntity<Resource> getDocument(@PathVariable String docPath) throws IOException {
        InputStreamResource resource = FileUtils.getDocument(docPath);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + docPath + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping(value = "/analyzePDF", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SheetMusicFinalResponseAIDTO> analyzeSheetMusicPDF(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty() || file.getBytes().length == 0) {
                return ResponseEntity.badRequest().build();
            }

            byte[] pdfBytes = file.getBytes();

            // 1️⃣ שלב AI – ניתוח PDF
            SheetMusicResponseAI aiResponse = agentService.analyzePdfBytes(pdfBytes);

            // 2️⃣ טיפול בכלי נגינה: הוספה ל-DB ובניית DTO עם ID
            List<InstrumentResponseDTO> finalInstrumentsDTO = new ArrayList<>();
            List<Instrument> instrumentsForSheet = new ArrayList<>();
            // aiResponse.instruments() מחזיר List<String>
            for (String instrumentName : aiResponse.instruments()) {
                // 2.1. חיפוש קיים לפי שם (פונקציית findByName ב-Repository)
                Instrument existing = instrumentRepository.findByName(instrumentName);
                if (existing == null) {
                    // 2.2. יצירת חדש אם לא קיים
                    existing = new Instrument();
                    existing.setName(instrumentName);
                    existing = instrumentRepository.save(existing); // שמירה ב-DB לקבלת ID
                }
                // 2.3. בניית ה-DTO הסופי עם ה-ID
                finalInstrumentsDTO.add(new InstrumentResponseDTO(existing.getId(), existing.getName()));
                instrumentsForSheet.add(existing);
            }

            // 3️⃣ בדיקה עבור קטגוריות – תמיכה במספר קטגוריות
            // 3️⃣ טיפול בקטגוריות: הוספה ל-DB ובניית DTO עם ID
            List<SheetMusicCategoryResponseDTO> finalCategoriesDTO = new ArrayList<>();
            List<SheetMusicCategory> categoriesForSheet = new ArrayList<>();
// aiResponse.suggestedCategory() מחזיר List<String>
            if (aiResponse.suggestedCategories() != null) {
                for (String categoryName : aiResponse.suggestedCategories()) {
                    // 3.1. חיפוש קיים לפי שם
                    SheetMusicCategory existingCat = categoryRepository.findByName(categoryName);
                    if (existingCat == null) {
                        // 3.2. יצירת חדש אם לא קיים
                        existingCat = new SheetMusicCategory();
                        existingCat.setName(categoryName);
                        existingCat = categoryRepository.save(existingCat); // שמירה ב-DB לקבלת ID
                    }
                    // 3.3. בניית ה-DTO הסופי עם ה-ID
                    finalCategoriesDTO.add(new SheetMusicCategoryResponseDTO(existingCat.getId(), existingCat.getName()));
                    categoriesForSheet.add(existingCat);
                }
            }

            // 4️⃣ מיפוי הסולם ורמת הקושי לאינאומים שלך
            EScale scaleEnum = null;
            if (aiResponse.scale() != null) {
                for (EScale scale : EScale.values()) {
                    if (scale.name().equalsIgnoreCase(aiResponse.scale())) {
                        scaleEnum = scale;
                        break;
                    }
                }
            }

            EDifficultyLevel levelEnum = null;
            if (aiResponse.difficulty() != null) {
                for (EDifficultyLevel level : EDifficultyLevel.values()) {
                    if (level.name().equalsIgnoreCase(aiResponse.difficulty())) {
                        levelEnum = level;
                        break;
                    }
                }
            }

//            // 5️⃣ שמירה בDB כ-SheetMusic (הקוד שבוטל חוזר לחיים)
//            SheetMusic sheet = new SheetMusic();
//            sheet.setTitle(aiResponse.title());
//            sheet.setScale(scaleEnum);
//            sheet.setLevel(levelEnum);
//            // sheet.setPages(pages); // שימוש במספר העמודים אם קיימת פונקציה
//            sheet.setInstruments(instrumentsForSheet);
//            sheet.setCategories(categoriesForSheet);
//            sheet.setFileName(file.getOriginalFilename());
//
//            // **תיקון: שימוש בשדות החדשים מה-DTO**
//            sheet.setComposer(aiResponse.composer());
//            sheet.setLyricist(aiResponse.lyricist());
//
////            sheet.setDateUploaded(LocalDate.now());
//
//            sheetMusicRepository.save(sheet); // שמירת ה-Entity המלא

            // 6️⃣ החזרת DTO מעודכן עם IDs
            SheetMusicFinalResponseAIDTO response = new SheetMusicFinalResponseAIDTO(
                    aiResponse.title(),
                    aiResponse.scale(),
                    finalInstrumentsDTO,
                    aiResponse.difficulty(),
                    finalCategoriesDTO, // כבר מכיל IDs
                    aiResponse.composer(),
                    aiResponse.lyricist()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            // ניתן להוסיף הודעת שגיאה ספציפית יותר כאן
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * פונקציה שמדמה ניתוח PDF עם AI.
     * כאן אפשר לשים את קריאה ל-Gemini או OpenAI.
     * חובה להחזיר את הנתונים באנגלית בלבד
     */
//    private SheetMusicResponseAI analyzePDFWithAI(byte[] pdfBytes) {
//        // 🔹 כרגע מחזיר דוגמה סטטית
//        return new SheetMusicResponseAI(
//                "Ode to Joy",                   // title
//                "C_MAJOR",                       // scale
//                List.of(new InstrumentResponseDTO(null, "Piano"), new InstrumentResponseDTO(null, "Violin")), // instruments
//                "BEGINNER",                      // difficulty
//                List.of(
//                        new SheetMusicCategoryResponseDTO(null, "Classical"),
//                        new SheetMusicCategoryResponseDTO(null, "Orchestral"),
//                        new SheetMusicCategoryResponseDTO(null, "Choir")
//                ), // suggestedCategory
//                "Ludwig van Beethoven",          // composer
//                "N/A"                            // lyricist
//        );
//    }

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


