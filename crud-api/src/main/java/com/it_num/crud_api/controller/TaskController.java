package com.it_num.crud_api.controller;

import com.it_num.crud_api.dto.request.create.TaskCreateRequest;
import com.it_num.crud_api.dto.request.update.TaskUpdateRequest;
import com.it_num.crud_api.dto.response.TaskResponse;
import com.it_num.crud_api.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskCreateRequest request) {
        return taskService.create(request);
    }

    @GetMapping("/{publicId}")
    public TaskResponse findById(@PathVariable UUID publicId) {
        return taskService.findByPublicId(publicId);
    }

    @GetMapping
    public List<TaskResponse> findAll(
            @RequestParam(required = false) UUID projectId) {
        if (projectId != null) {
            return taskService.findByProject(projectId);
        }
        return taskService.findAll();
    }

    @PutMapping("/{publicId}")
    public TaskResponse update(
            @PathVariable UUID publicId,
            @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.update(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID publicId) {
        taskService.delete(publicId);
    }
}