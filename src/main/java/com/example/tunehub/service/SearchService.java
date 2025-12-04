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
        return usersRepository.findAllByNameContainingIgnoreCase(q)
                .stream()
                .map(u -> new UsersDTO(u.getId(), u.getName()))
                .toList();
    }


}
