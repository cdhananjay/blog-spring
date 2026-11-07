package com.example.blog.services;

import com.example.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    Set<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID id);
    Tag findById(UUID id);
    List<Tag> findTagsByIds(Set<UUID> ids);
}
