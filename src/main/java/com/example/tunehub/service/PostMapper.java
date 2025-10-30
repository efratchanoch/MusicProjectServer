package com.example.tunehub.service;

import com.example.tunehub.dto.PostDTO;
import com.example.tunehub.dto.PostMediaDTO;
import com.example.tunehub.model.Post;
import org.mapstruct.Mapper;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    List<PostMediaDTO> PostToDTO(List<Post> posts);
    Post PostMediaDTOtoPost(PostMediaDTO p);

    Post PostDTOtoPost(PostDTO p);
//    PostDTO PosttoPostDTO(Post p);
//    List<PostDTO> PostsToPostsDTO(List<Post> p);


    default PostMediaDTO PostToDTO(Post p) throws IOException {
        PostMediaDTO postMediaDTO=new PostMediaDTO();
        postMediaDTO.setId(p.getId());

        return postMediaDTO;
    }

}
