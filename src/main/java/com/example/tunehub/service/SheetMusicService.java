package com.example.tunehub.service;

import com.example.tunehub.dto.SheetMusicResponseDTO;
import com.example.tunehub.dto.SheetMusicUploadDTO;
import com.example.tunehub.model.Instrument;
import com.example.tunehub.model.SheetMusic;
import com.example.tunehub.model.SheetMusicCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SheetMusicService {

    private final SheetMusicRepository sheetMusicRepository;
    private final InstrumentRepository instrumentRepository;
    private final SheetMusicCategoryRepository categoryRepository;
    private final SheetMusicMapper sheetMusicMapper;
    private final AuthService authService;

    public SheetMusicResponseDTO upload(SheetMusicUploadDTO dto, MultipartFile file, MultipartFile image) throws Exception {

        // 1. מיפוי בסיסי מה-DTO (ללא instruments ו-categories)
        SheetMusic s = sheetMusicMapper.SheetMusicUploadDTOtoSheetMusic(dto);

        // 2. הגדרת user
        s.setUser(authService.getCurrentUser());

        // 3. יצירת שמות ייחודיים
        String uniqueFileName = null;
        if (file != null) {
            uniqueFileName = FileUtils.generateUniqueFileName(file);
            s.setFileName(uniqueFileName);
        }

        String uniqueImageCoverName = null;
        if (image != null) {
            uniqueImageCoverName = FileUtils.generateUniqueFileName(image);
            s.setImageCoverName(uniqueImageCoverName);
        }

        // 4. ספירת עמודים
        s.setPages(FileUtils.getPDFPageCount(file.getBytes()));

        // 5. טעינת Instrument לפי ID
        List<Instrument> instruments =
                dto.instruments().stream()
                        .map(i -> instrumentRepository.findById(i.id())
                                .orElseThrow(() -> new RuntimeException("Instrument not found: id=" + i.id())))
                        .toList();
        s.setInstruments(instruments);

        // 6. טעינת Categories לפי ID
        List<SheetMusicCategory> categories =
                dto.categories().stream()
                        .map(c -> categoryRepository.findById(c.id())
                                .orElseThrow(() -> new RuntimeException("Category not found: id=" + c.id())))
                        .toList();
        s.setCategories(categories);

        // 7. שמירה ב-DB
        sheetMusicRepository.save(s);

        // 8. שמירת קבצים פיזית
        FileUtils.uploadDocument(file, uniqueFileName);
        FileUtils.uploadImage(image, uniqueImageCoverName);

        // 9. מיפוי ל-ResponseDTO
        return sheetMusicMapper.sheetMusicToSheetMusicResponseDTO(s);
    }
}
