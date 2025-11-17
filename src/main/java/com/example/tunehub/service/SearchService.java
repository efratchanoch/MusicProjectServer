package com.example.tunehub.service;


import com.example.tunehub.dto.PostResponseDTO;
import com.example.tunehub.dto.UsersDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private  UsersRepository usersRepository;
    private  PostRepository postRepository;
    private  SheetMusicRepository sheetMusicRepository;

    public SearchService(UsersRepository usersRepository, PostRepository postRepository, SheetMusicRepository sheetMusicRepository) {
        this.usersRepository = usersRepository;
        this.postRepository = postRepository;
        this.sheetMusicRepository = sheetMusicRepository;
    }

    public List<UsersDTO> searchUsers(String q) {
        if (q == null || q.isBlank()) return List.of();
        return usersRepository.findByNameContainingIgnoreCase(q)
                .stream()
                .map(u -> new UsersDTO(u.getId(), u.getName()))
                .toList();
    }

//    public List<PostResponseDTO> searchPosts(String q) {
//        if (q == null || q.isBlank()) return List.of();
//        return postRepo.searchByTitleOrContent(q)
//                .stream()
//                .map(PostDTO::fromEntity)
//                .toList();
//    }
//
//    public List<SheetDTO> searchSheets(String q) {
//        if (q == null || q.isBlank()) return List.of();
//        return sheetRepo.findByNameContaining(q)
//                .stream()
//                .map(SheetDTO::fromEntity)
//                .toList();
//    }
//
//    public SearchResult searchAll(String q) {
//        return new SearchResult(
//                searchUsers(q),
//                searchPosts(q),
//                searchSheets(q)
//        );
//    }


}
