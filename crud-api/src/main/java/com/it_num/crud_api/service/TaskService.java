package com.it_num.crud_api.service;

import com.it_num.crud_api.domain.Project;
import com.it_num.crud_api.domain.Tag;
import com.it_num.crud_api.domain.Task;
import com.it_num.crud_api.dto.request.create.TaskCreateRequest;
import com.it_num.crud_api.dto.request.update.TaskUpdateRequest;
import com.it_num.crud_api.dto.response.TaskResponse;
import com.it_num.crud_api.exception.ResourceNotFoundException;
import com.it_num.crud_api.mapper.TaskMapper;
import com.it_num.crud_api.repository.ProjectRepository;
import com.it_num.crud_api.repository.TagRepository;
import com.it_num.crud_api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task. Project must exist; tags (if any) must all exist.
     *
     * @throws ResourceNotFoundException if the project or any provided tag does not exist
     */
    public TaskResponse create(TaskCreateRequest request) {
        Project project = projectRepository.findByPublicId(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", request.projectId()));

        Set<Tag> tags = resolveTags(request.tagIds());

        Task task = taskMapper.toEntity(request, project, tags);
        Task saved = taskRepository.save(task);
        return taskMapper.toResponse(saved);
    }

    /**
     * Retrieves a task by its public UUID.
     *
     * @throws ResourceNotFoundException if no task matches the given publicId
     */
    @Transactional(readOnly = true)
    public TaskResponse findByPublicId(UUID publicId) {
        Task task = taskRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", publicId));
        return taskMapper.toResponse(task);
    }

    /**
     * Retrieves all tasks.
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves all tasks belonging to a specific project.
     *
     * @throws ResourceNotFoundException if the project does not exist
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> findByProject(UUID projectPublicId) {
        if (projectRepository.findByPublicId(projectPublicId).isEmpty()) {
            throw new ResourceNotFoundException("Project", projectPublicId);
        }
        return taskRepository.findByProject_PublicId(projectPublicId).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    /**
     * Updates an existing task identified by publicId.
     * Project is not changeable here; tags can be replaced.
     *
     * @throws ResourceNotFoundException if the task or any provided tag does not exist
     */
    public TaskResponse update(UUID publicId, TaskUpdateRequest request) {
        Task task = taskRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", publicId));

        Set<Tag> newTags = (request.tagIds() != null) ? resolveTags(request.tagIds()) : null;

        taskMapper.updateEntity(task, request, newTags);
        Task updated = taskRepository.save(task);
        return taskMapper.toResponse(updated);
    }

    /**
     * Deletes a task by its public UUID.
     *
     * @throws ResourceNotFoundException if no task matches the given publicId
     */
    public void delete(UUID publicId) {
        Task task = taskRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", publicId));
        taskRepository.delete(task);
    }

    /**
     * Resolves a set of tag publicIds into actual Tag entities.
     * Throws if any provided tag does not exist (all-or-nothing).
     */
    private Set<Tag> resolveTags(Set<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Tag> foundTags = tagRepository.findByPublicIdIn(tagIds);

        if (foundTags.size() != tagIds.size()) {
            Set<UUID> foundIds = foundTags.stream()
                    .map(Tag::getPublicId)
                    .collect(java.util.stream.Collectors.toSet());

            Set<UUID> missingIds = new HashSet<>(tagIds);
            missingIds.removeAll(foundIds);

            throw new ResourceNotFoundException(
                    "Some tags not found: " + missingIds
            );
        }

        return new HashSet<>(foundTags);
    }
}