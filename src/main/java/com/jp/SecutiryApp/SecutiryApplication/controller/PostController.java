package com.jp.SecutiryApp.SecutiryApplication.controller;

import com.jp.SecutiryApp.SecutiryApplication.dto.PostDTO;
import com.jp.SecutiryApp.SecutiryApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {

    private  final PostService postService;

    @GetMapping
    @Secured({"ROLE_USER" , "ROLE_ADMIN"})
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
//    @PreAuthorize("hasAnyRole('USER','ADMIN') AND hasAuthority('POST_VIEW')") //here we doint need suffix as suffix is inbuilt in PreAuthorize
    @PreAuthorize("@postSecurity.isOwnerOfThePost(#postId)") // taking the details from post security class
    public PostDTO getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost) {
        return postService.createNewPost(inputPost);
    }

}
