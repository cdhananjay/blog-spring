package com.example.blog.controllers;

import com.example.blog.domain.dtos.CreateTagRequestDto;
import com.example.blog.domain.dtos.TagResponseDto;
import com.example.blog.mappers.TagMapper;
import com.example.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagMapper tagMapper;
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<Set<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getTags().stream().map(tagMapper::toTagResponse).collect(Collectors.toSet()));
    }

    @PostMapping
    public ResponseEntity<List<TagResponseDto>> createTags(@Valid @RequestBody CreateTagRequestDto createTagRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                tagService.createTags(createTagRequestDto.getTags()).stream().map(tagMapper::toTagResponse).toList()
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable ("id") UUID id) {
        tagService.deleteTag(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
