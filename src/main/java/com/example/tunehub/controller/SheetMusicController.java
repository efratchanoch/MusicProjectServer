package com.example.tunehub.controller;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.*;
import com.example.tunehub.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sheetMusic")
public class SheetMusicController {
    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private SheetMusicRepository sheetMusicRepository;
    private SheetMusicMapper sheetMusicMapper;

    @Autowired
    public SheetMusicController(SheetMusicRepository sheetMusicRepository, SheetMusicMapper sheetMusicMapper, UsersMapper usersMapper, UsersRepository usersRepository) {
        this.sheetMusicRepository = sheetMusicRepository;
        this.sheetMusicMapper = sheetMusicMapper;
        this.usersMapper = usersMapper;
        this.usersRepository = usersRepository;
    }

    //Get
    @GetMapping("/sheetMusicById/{id}")
    public ResponseEntity<SheetMusicResponseDTO> getSheetMusicById(@PathVariable Long id) {
        try {
            SheetMusic s = sheetMusicRepository.findSheetMusicById(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusic")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusic() {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAll();
            if (s.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/sheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUser_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favoriteSheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getFavoriteSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUsersFavorite_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByName/{name}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByName(String name) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByName(name);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByCategory/{category_id}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByCategory(@PathVariable Long category_id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByCategory_Id(category_id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByScale/{scale}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByScale(@PathVariable EScale scale) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByScale(scale);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByLevel/{level}")
    public ResponseEntity<List<SheetMusicResponseDTO>> getSheetsMusicByLevel(@PathVariable EDifficultyLevel level) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByLevel(level);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(sheetMusicMapper.sheetMusicListToSheetMusicResponseDTOlist(s), HttpStatus.OK);
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

    @DeleteMapping("/sheetsMusicByCategoryId/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN' ,'ROLE_SUPER_ADMIN')")
    public ResponseEntity deleteAllSheetsMusicByCategoryId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByCategoryId(id);
            if (s == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            sheetMusicRepository.deleteAllByCategoryId(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/uploadSheetMusic")
    public ResponseEntity<SheetMusicResponseDTO> uploadSheetMusic(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") SheetMusicUploadDTO dto) {
        try {
            // 1. יצירת Entity מה-DTO
            SheetMusic s = sheetMusicMapper.SheetMusicUploadDTOtoSheetMusic(dto);

            // 2. יצירת שם ייחודי לקובץ
            String uniqueFileName = FileUtils.generateUniqueFileName(file);
            s.setFileName(uniqueFileName);

            // 3. קבלת User reference מה-ID ב-DTO
            Long userId = dto.getUser().getId();
            Users userReference = usersRepository.getReferenceById(userId);
            s.setUser(userReference);

            // 4. ספירת עמודי PDF
            s.setPages(FileUtils.getPDFPageCount(file.getBytes()));

            // 5. שמירת ה-Entity במסד
            sheetMusicRepository.save(s);

            // 6. שמירת הקובץ (הקובץ נשמר בנפרד, לא ב-DTO)
            FileUtils.uploadDocument(file, uniqueFileName);

            // 7. החזרת ה-ResponseDTO
            SheetMusicResponseDTO responseDTO = sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(s);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    // 2. קבלת דף מוזיקה יחיד
//    @GetMapping("/{id}")
//    public ResponseEntity<SheetMusicResponseDTO> getSheetMusic(@PathVariable Long id) {
//        SheetMusic sheetMusic = sheetMusicService.getSheetMusicById(id);
//        return ResponseEntity.ok(convertToDto(sheetMusic));
//    }
//
//    // 3. קבלת רשימת דפי מוזיקה
//    @GetMapping
//    public ResponseEntity<List<SheetMusicResponseDTO>> getAllSheetMusic() {
//        List<SheetMusic> list = sheetMusicService.getAllSheetMusic();
//        return ResponseEntity.ok(convertListToDto(list));
//    }
//
//    // 4. הורדת הקובץ הפיזי (PDF/תמונה)
//    @GetMapping("/{id}/download")
//    public ResponseEntity<Resource> downloadSheetMusic(@PathVariable Long id) {
//        Resource resource = sheetMusicService.downloadSheetMusic(id);
//
//        // הגדרת כותרות מתאימות להורדה...
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }
//@GetMapping("/docs/{docPath}")
//public ResponseEntity<Resource> getDocument(@PathVariable String docPath) throws IOException {
//    InputStreamResource resource = new InputStreamResource(
//            Files.newInputStream(Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, docPath))
//    );
//    return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + docPath + "\"")
//            .contentType(MediaType.APPLICATION_PDF)
//            .body(resource);
//}

}
