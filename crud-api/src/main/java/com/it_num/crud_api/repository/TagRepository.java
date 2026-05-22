package com.it_num.crud_api.repository;

import com.it_num.crud_api.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByPublicId(UUID publicId);

    boolean existsByName(String name);

    List<Tag> findByPublicIdIn(Set<UUID> publicIds);
}