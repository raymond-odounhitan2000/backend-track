package com.it_num.crud_api.repository;

import com.it_num.crud_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPublicId(UUID publicId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}