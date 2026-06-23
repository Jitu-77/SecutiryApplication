package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);
}
