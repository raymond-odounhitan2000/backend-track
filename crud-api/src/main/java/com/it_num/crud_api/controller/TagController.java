package com.it_num.crud_api.controller;

import com.it_num.crud_api.dto.request.create.TagCreateRequest;
import com.it_num.crud_api.dto.request.update.TagUpdateRequest;
import com.it_num.crud_api.dto.response.TagResponse;
import com.it_num.crud_api.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@Valid @RequestBody TagCreateRequest request) {
        return tagService.create(request);
    }

    @GetMapping("/{publicId}")
    public TagResponse findById(@PathVariable UUID publicId) {
        return tagService.findByPublicId(publicId);
    }

    @GetMapping
    public List<TagResponse> findAll() {
        return tagService.findAll();
    }

    @PutMapping("/{publicId}")
    public TagResponse update(
            @PathVariable UUID publicId,
            @Valid @RequestBody TagUpdateRequest request) {
        return tagService.update(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID publicId) {
        tagService.delete(publicId);
    }
}