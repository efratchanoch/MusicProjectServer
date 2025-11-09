package com.example.tunehub.controller;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.*;
import com.example.tunehub.service.FileUtils;
import com.example.tunehub.service.SheetMusicMapper;
import com.example.tunehub.service.SheetMusicRepository;
import com.example.tunehub.service.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SheetMusicRepository sheetMusicRepository;
    private SheetMusicMapper sheetMusicMapper;

    @Autowired
    public SheetMusicController(SheetMusicRepository sheetMusicRepository, SheetMusicMapper sheetMusicMapper, UsersMapper usersMapper) {
        this.sheetMusicRepository = sheetMusicRepository;
        this.sheetMusicMapper = sheetMusicMapper;
        this.usersMapper = usersMapper;
    }

    //Get
    @GetMapping("/sheetMusicById/{id}")
    public ResponseEntity<SheetMusic> getSheetMusicById(@PathVariable Long id) {
        try {
            SheetMusic s = sheetMusicRepository.findSheetMusicById(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusic")
    public ResponseEntity<List<SheetMusic>> getSheetsMusic() {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAll();
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/sheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUser_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/favoriteSheetsMusicByUserId/{id}")
    public ResponseEntity<List<SheetMusic>> getFavoriteSheetsMusicByUserId(@PathVariable Long id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByUsersFavorite_Id(id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByName/{name}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByName(String name) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllByName(name);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByCategory/{category_id}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByCategory(@PathVariable Long category_id) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByCategory_Id(category_id);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByScale/{scale}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByScale(@PathVariable EScale scale) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByScale(scale);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sheetsMusicByLevel/{level}")
    public ResponseEntity<List<SheetMusic>> getSheetsMusicByLevel(@PathVariable EDifficultyLevel level) {
        try {
            List<SheetMusic> s = sheetMusicRepository.findAllSheetMusicByLevel(level);
            if (s == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(s, HttpStatus.OK);
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
    public ResponseEntity<SheetMusic> uploadSheetMusic(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") SheetMusicUploadDTO dto) {
        try {
            FileUtils.uploadDocument(file);
            dto.setFileName(file.getOriginalFilename());
            SheetMusic s = sheetMusicMapper.SheetMusicUploadDTOtoSheetMusic(dto);
            sheetMusicRepository.save(s);
            return new ResponseEntity<>(s, HttpStatus.CREATED);
        } catch (Exception e) {
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
}
