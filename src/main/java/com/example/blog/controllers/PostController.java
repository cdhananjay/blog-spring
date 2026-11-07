package com.example.blog.controllers;

import com.example.blog.domain.dtos.CreatePostRequestDto;
import com.example.blog.domain.dtos.PostDto;
import com.example.blog.domain.dtos.UpdatePostRequestDto;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.User;
import com.example.blog.mappers.PostMapper;
import com.example.blog.services.PostService;
import com.example.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postMapper.toPostDto(
                postService.getPostById(id)
        ));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
    ) {
        return ResponseEntity.ok(postService.getAllPosts(categoryId, tagId).stream().map(postMapper::toPostDto).toList());
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDraftPosts(
        @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);
        return ResponseEntity.ok(postService.getDraftPosts(loggedInUser).stream().map(postMapper::toPostDto).toList());
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            ) {
        User loggedInUser = userService.getUserById(userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postMapper.toPostDto(
                        postService.createPost(loggedInUser, postMapper.toCreatePostRequest(createPostRequestDto))
                        )
                );
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
            ) {
        return ResponseEntity.ok(postMapper.toPostDto(
                postService.updatePost(id, postMapper.toUpdatePostRequest(updatePostRequestDto))
        ));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") UUID id
    ) {
        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
