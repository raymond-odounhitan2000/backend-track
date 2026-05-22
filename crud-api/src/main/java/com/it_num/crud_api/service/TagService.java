package com.it_num.crud_api.service;

import com.it_num.crud_api.domain.Tag;
import com.it_num.crud_api.dto.request.create.TagCreateRequest;
import com.it_num.crud_api.dto.request.update.TagUpdateRequest;
import com.it_num.crud_api.dto.response.TagResponse;
import com.it_num.crud_api.exception.ResourceNotFoundException;
import com.it_num.crud_api.mapper.TagMapper;
import com.it_num.crud_api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Creates a new tag after validating name uniqueness.
     *
     * @throws IllegalArgumentException if a tag with the same name already exists
     */
    public TagResponse create(TagCreateRequest request) {
        if (tagRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Tag name already exists: " + request.name());
        }

        Tag tag = tagMapper.toEntity(request);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toResponse(saved);
    }

    /**
     * Retrieves a tag by its public UUID.
     *
     * @throws ResourceNotFoundException if no tag matches the given publicId
     */
    @Transactional(readOnly = true)
    public TagResponse findByPublicId(UUID publicId) {
        Tag tag = tagRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", publicId));
        return tagMapper.toResponse(tag);
    }

    /**
     * Retrieves all tags.
     */
    @Transactional(readOnly = true)
    public List<TagResponse> findAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    /**
     * Updates an existing tag identified by publicId.
     *
     * @throws ResourceNotFoundException if no tag matches the given publicId
     * @throws IllegalArgumentException  if the new name is already taken by another tag
     */
    public TagResponse update(UUID publicId, TagUpdateRequest request) {
        Tag tag = tagRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", publicId));

        // Check name uniqueness only if name has changed
        if (!tag.getName().equals(request.name())
                && tagRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Tag name already exists: " + request.name());
        }

        tagMapper.updateEntity(tag, request);
        Tag updated = tagRepository.save(tag);
        return tagMapper.toResponse(updated);
    }

    /**
     * Deletes a tag by its public UUID.
     * Note: this removes the tag from all tasks that were using it (via the join table).
     *
     * @throws ResourceNotFoundException if no tag matches the given publicId
     */
    public void delete(UUID publicId) {
        Tag tag = tagRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", publicId));
        tagRepository.delete(tag);
    }
}