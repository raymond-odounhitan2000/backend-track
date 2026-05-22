package com.it_num.crud_api.mapper;

import com.it_num.crud_api.domain.Project;
import com.it_num.crud_api.domain.Tag;
import com.it_num.crud_api.domain.Task;
import com.it_num.crud_api.domain.enums.Priority;
import com.it_num.crud_api.domain.enums.TaskStatus;
import com.it_num.crud_api.dto.request.create.TaskCreateRequest;
import com.it_num.crud_api.dto.request.update.TaskUpdateRequest;
import com.it_num.crud_api.dto.response.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest request, Project project, Set<Tag> tags) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setProject(project);
        task.setStatus(request.status() != null ? request.status() : TaskStatus.TODO);
        task.setPriority(request.priority() != null ? request.priority() : Priority.MEDIUM);
        task.setDueDate(request.dueDate());

        if (tags != null) {
            tags.forEach(task::addTag);
        }

        return task;
    }

    public void updateEntity(Task task, TaskUpdateRequest request, Set<Tag> newTags) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : task.getStatus());
        task.setPriority(request.priority() != null ? request.priority() : task.getPriority());
        task.setDueDate(request.dueDate());

        if (newTags != null) {
            Set<Tag> currentTags = new HashSet<>(task.getTags());
            currentTags.forEach(task::removeTag);
            newTags.forEach(task::addTag);
        }
    }

    public TaskResponse toResponse(Task task) {
        Project project = task.getProject();

        TaskResponse.ProjectSummary projectSummary = new TaskResponse.ProjectSummary(
                project.getPublicId(),
                project.getName()
        );

        Set<TaskResponse.TagSummary> tagSummaries = task.getTags().stream()
                .map(tag -> new TaskResponse.TagSummary(
                        tag.getPublicId(),
                        tag.getName(),
                        tag.getColor()
                ))
                .collect(Collectors.toSet());

        return new TaskResponse(
                task.getPublicId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                projectSummary,
                tagSummaries,
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}