package com.example.blog.services.impl;

import com.example.blog.domain.entities.Tag;
import com.example.blog.repositories.TagRepository;
import com.example.blog.services.TagService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Set<Tag> getTags() {
        return tagRepository.finalAllWithPostCount();
    }

    @Override
    @Transactional
    public List<Tag> createTags(Set<String> tagNames) {
        Set<Tag> tags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        Set<Tag> tagsToSave = tagNames.stream().filter(
                name -> !existingTagNames.contains(name)
        ).map(s -> new Tag(null, s, Set.of())).collect(Collectors.toSet());
        List<Tag> savedTags = tagRepository.saveAll(tagsToSave);
        savedTags.addAll(tags.stream().toList());
        return savedTags;
    }

    @Override
    @Transactional
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(
                tag -> {
                    if (!tag.getPosts().isEmpty())
                        throw new IllegalStateException("Cannot delete tag with post.");
                    tagRepository.deleteById(id);
                }
        );
    }

    @Override
    public Tag findById(UUID id) {
        return tagRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Tag not found with id " + id)
        );
    }

    @Override
    public List<Tag> findTagsByIds(Set<UUID> ids) {
        List<Tag> tags = tagRepository.findAllById(ids);
        if (tags.size() != ids.size() ) throw new IllegalArgumentException("Tag ids are not valid.");
        return tags;
    }
}
