package com.it_num.crud_api.service;

import com.it_num.crud_api.domain.Project;
import com.it_num.crud_api.domain.User;
import com.it_num.crud_api.dto.request.create.ProjectCreateRequest;
import com.it_num.crud_api.dto.request.update.ProjectUpdateRequest;
import com.it_num.crud_api.dto.response.ProjectResponse;
import com.it_num.crud_api.exception.ResourceNotFoundException;
import com.it_num.crud_api.mapper.ProjectMapper;
import com.it_num.crud_api.repository.ProjectRepository;
import com.it_num.crud_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectResponse create(ProjectCreateRequest request) {
        User owner = userRepository.findByPublicId(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.ownerId()));

        Project project = projectMapper.toEntity(request, owner);
        Project saved = projectRepository.save(project);
        return projectMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public ProjectResponse findByPublicId(UUID publicId) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", publicId));
        return projectMapper.toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByOwner(UUID ownerPublicId) {
        if (userRepository.findByPublicId(ownerPublicId).isEmpty()) {
            throw new ResourceNotFoundException("User", ownerPublicId);
        }
        return projectRepository.findByOwner_PublicId(ownerPublicId).stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    public ProjectResponse update(UUID publicId, ProjectUpdateRequest request) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", publicId));

        projectMapper.updateEntity(project, request);
        Project updated = projectRepository.save(project);
        return projectMapper.toResponse(updated);
    }

    public void delete(UUID publicId) {
        Project project = projectRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", publicId));
        projectRepository.delete(project);
    }
}