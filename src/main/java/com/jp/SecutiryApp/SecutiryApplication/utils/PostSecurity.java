package com.jp.SecutiryApp.SecutiryApplication.utils;

import com.jp.SecutiryApp.SecutiryApplication.dto.PostDTO;
import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import com.jp.SecutiryApp.SecutiryApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    public final PostService postService;

    public boolean isOwnerOfThePost(Long postId){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());

    }
}
