package com.example.tunehub.service;

import com.example.tunehub.dto.*;
import com.example.tunehub.model.Post;
import com.example.tunehub.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring", uses = UsersMapper.class)
public interface PostMapper {
    @Mapping(target = "dateUploaded", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hearts", constant = "0")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "usersFavorite", ignore = true)
    Post postUploadDTOtoPost(PostUploadDTO dto);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "imagesBase64", expression = "java(com.example.tunehub.service.FileUtils.imagesToBase64(post.getImagesPath()))")
    PostResponseDTO postToPostResponseDTO(Post post);


    List<PostResponseDTO> postListToPostResponseDTOlist(List<Post> post);



    List<PostMediaDTO> PostToDTO(List<Post> posts);

    Post PostMediaDTOtoPost(PostMediaDTO p);

    Post PostDTOtoPost(PostDTO p);

    //    PostDTO PosttoPostDTO(Post p);
    List<PostDTO> PostsToPostsDTO(List<Post> p);


//    default PostMediaDTO PostToDTO(Post p) throws IOException {
//        PostMediaDTO postMediaDTO = new PostMediaDTO();
//        postMediaDTO.setId(p.getId());
//        return postMediaDTO;
//    }

//    default PostDTO PostToDTO(Post p) throws IOException {
//        PostDTO postDTO =new PostDTO();
//        postDTO.setMedia(postMediaDTO.setImagePath);
//        PostDTO.setImagePath(p.getImageProfilePath());
//        return postDTO;
//    }

}
