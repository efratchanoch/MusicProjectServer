package com.example.tunehub.service;

import com.example.tunehub.dto.GlobalSearchResponseDTO;
import com.example.tunehub.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class GeneralSearchService {

    private final TeacherMapper teacherMapper;
    //     Inject Repositories (כולל LikeRepository ו-FavoriteRepository אם צריך)
    private PostRepository postRepository;
    private SheetMusicRepository sheetMusicRepository;
    private UsersRepository usersRepository;
    private TeacherRepository teacherRepository;
    private SheetMusicCategoryRepository sheetMusicCategoryRepository;
    private LikeRepository likeRepository;
    private FavoriteRepository favoriteRepository;

    // Inject Mappers and Services
    private AuthService authService;
    private PostMapper postMapper;
    private SheetMusicMapper sheetMusicMapper;
    private UsersMapper usersMapper;
    private SheetMusicCategoryMapper sheetMusicCategoryMapper;

    @Autowired
    public GeneralSearchService(PostRepository postRepository, SheetMusicRepository sheetMusicRepository, UsersRepository usersRepository, TeacherRepository teacherRepository, SheetMusicCategoryRepository sheetMusicCategoryRepository, LikeRepository likeRepository, FavoriteRepository favoriteRepository, AuthService authService, PostMapper postMapper, SheetMusicMapper sheetMusicMapper, UsersMapper usersMapper, SheetMusicCategoryMapper sheetMusicCategoryMapper, TeacherMapper teacherMapper) {
        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
        this.usersRepository = usersRepository;
        this.teacherRepository = teacherRepository;
        this.sheetMusicCategoryRepository = sheetMusicCategoryRepository;
        this.likeRepository = likeRepository;
        this.favoriteRepository = favoriteRepository;
        this.authService = authService;
        this.postMapper = postMapper;
        this.sheetMusicMapper = sheetMusicMapper;
        this.usersMapper = usersMapper;
        this.sheetMusicCategoryMapper = sheetMusicCategoryMapper;
        this.teacherMapper = teacherMapper;
    }

    public GlobalSearchResponseDTO executeGlobalSearch(String searchTerm) {
        // קבלת ID המשתמש הנוכחי לצורך חישוב לייקים/מועדפים
        //Long currentUserId = authService.getCurrentUserId();
        GlobalSearchResponseDTO results = new GlobalSearchResponseDTO();

        // 1. חיפוש פוסטים (מוגבל)
        List<Post> posts = postRepository.findAllTop5ByTitleContainingIgnoreCase(searchTerm);
        if (!posts.isEmpty()) {
            results.setPosts(postMapper.postListToPostSearchDTOList(posts));
        }

        // 2. חיפוש תווים (מוגבל)
        List<SheetMusic> sheets = sheetMusicRepository.findAllTop5ByTitleContainingIgnoreCase(searchTerm);
        if (sheets != null && !sheets.isEmpty()) {
            results.setSheetMusic(sheetMusicMapper.sheetMusicListToSheetMusicSearchDTOList(sheets));
        }

        // 3. חיפוש מוזיקאים (מוגבל)
        List<Users> users = usersRepository.findAllTop5ByNameContainingIgnoreCase(searchTerm);
        if (users != null && !users.isEmpty()) {
            results.setMusicians(usersMapper.usersListToUsersSearchDTOList(users));
        }

        // 4. חיפוש מורים (מוגבל)
        List<Teacher> teachers = teacherRepository.findAllTop5ByNameContainingIgnoreCase(searchTerm);
        if (teachers != null && !teachers.isEmpty()) {
            results.setTeachers(teacherMapper.teacherListToUsersSearchDTOList(teachers));
        }


        return results;
    }
}