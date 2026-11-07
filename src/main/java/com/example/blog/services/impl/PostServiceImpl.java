package com.example.blog.services.impl;

import com.example.blog.domain.CreatePostRequest;
import com.example.blog.domain.PostStatus;
import com.example.blog.domain.UpdatePostRequest;
import com.example.blog.domain.entities.Category;
import com.example.blog.domain.entities.Post;
import com.example.blog.domain.entities.Tag;
import com.example.blog.domain.entities.User;
import com.example.blog.repositories.PostRepository;
import com.example.blog.services.CategoryService;
import com.example.blog.services.PostService;
import com.example.blog.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Override
    @Transactional
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.findById(categoryId);
            Tag tag = tagService.findById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if (categoryId != null) {
            Category category = categoryService.findById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }
        if (tagId != null) {
            Tag tag = tagService.findById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }
        return postRepository.findAllByStatus(
                PostStatus.PUBLISHED
        );
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        return postRepository.save(
            new Post(
                    null,
                    createPostRequest.getTitle(),
                    createPostRequest.getContent(),
                    createPostRequest.getStatus(),
                    calculateReadingTime(createPostRequest.getContent()),
                    user,
                    categoryService.findById(createPostRequest.getCategoryId()),
                    new HashSet<>(tagService.findTagsByIds(createPostRequest.getTagIds())),
                    null,
                    null
            )
        );
    }

    @Transactional
    @Override
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(updatePostRequest.getId()).orElseThrow(
                () -> new IllegalArgumentException("Post does not exist with id " + updatePostRequest.getId())
        );
        post.setTitle(updatePostRequest.getTitle());
        post.setContent(updatePostRequest.getContent());
        post.setStatus(updatePostRequest.getStatus());
        post.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));
        post.setCategory(categoryService.findById(updatePostRequest.getCategoryId()));
        post.setTags(new HashSet<>(tagService.findTagsByIds(updatePostRequest.getTagIds())));
        return postRepository.save(post);
    }

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid post id.")
        );
    }

    @Override
    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }

    public int calculateReadingTime(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        int wordCount = text.trim().split("\\s+").length;
        int wordsPerMinute = 200;
        return (int) Math.ceil((double) wordCount / wordsPerMinute);
    }
}
