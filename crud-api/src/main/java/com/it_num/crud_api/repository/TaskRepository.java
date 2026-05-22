package com.it_num.crud_api.repository;

import com.it_num.crud_api.domain.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"project", "tags"})
    Optional<Task> findByPublicId(UUID publicId);

    @EntityGraph(attributePaths = {"project", "tags"})
    @Override
    List<Task> findAll();

    @EntityGraph(attributePaths = {"project", "tags"})
    List<Task> findByProject_PublicId(UUID projectPublicId);

    long countByProject_PublicId(UUID projectPublicId);
}