package com.it_num.crud_api.mapper;

import com.it_num.crud_api.domain.Project;
import com.it_num.crud_api.domain.User;
import com.it_num.crud_api.domain.enums.ProjectStatus;
import com.it_num.crud_api.dto.request.create.ProjectCreateRequest;
import com.it_num.crud_api.dto.request.update.ProjectUpdateRequest;
import com.it_num.crud_api.dto.response.ProjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(ProjectCreateRequest request, User owner) {
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setOwner(owner);
        project.setStatus(request.status() != null ? request.status() : ProjectStatus.ACTIVE);
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
        return project;
    }

    public void updateEntity(Project project, ProjectUpdateRequest request) {
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status() != null ? request.status() : project.getStatus());
        project.setStartDate(request.startDate());
        project.setEndDate(request.endDate());
    }

    public ProjectResponse toResponse(Project project) {
        User owner = project.getOwner();

        ProjectResponse.OwnerSummary ownerSummary = new ProjectResponse.OwnerSummary(
                owner.getPublicId(),
                owner.getUsername(),
                owner.getFullName()
        );

        return new ProjectResponse(
                project.getPublicId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                ownerSummary,
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}