package com.it_num.crud_api.controller;

import com.it_num.crud_api.dto.request.create.ProjectCreateRequest;
import com.it_num.crud_api.dto.request.update.ProjectUpdateRequest;
import com.it_num.crud_api.dto.response.ProjectResponse;
import com.it_num.crud_api.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.create(request);
    }

    @GetMapping("/{publicId}")
    public ProjectResponse findById(@PathVariable UUID publicId) {
        return projectService.findByPublicId(publicId);
    }

    @GetMapping
    public List<ProjectResponse> findAll(
            @RequestParam(required = false) UUID ownerId) {
        if (ownerId != null) {
            return projectService.findByOwner(ownerId);
        }
        return projectService.findAll();
    }

    @PutMapping("/{publicId}")
    public ProjectResponse update(
            @PathVariable UUID publicId,
            @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.update(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID publicId) {
        projectService.delete(publicId);
    }
}