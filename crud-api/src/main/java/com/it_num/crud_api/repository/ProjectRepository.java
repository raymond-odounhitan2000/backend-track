package com.it_num.crud_api.repository;

import com.it_num.crud_api.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByPublicId(UUID publicId);

    List<Project> findByOwner_PublicId(UUID ownerPublicId);
}