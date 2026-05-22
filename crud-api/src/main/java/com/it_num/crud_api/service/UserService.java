package com.it_num.crud_api.service;

import com.it_num.crud_api.domain.User;
import com.it_num.crud_api.dto.request.create.UserCreateRequest;
import com.it_num.crud_api.dto.request.update.UserUpdateRequest;
import com.it_num.crud_api.dto.response.UserResponse;
import com.it_num.crud_api.exception.ResourceNotFoundException;
import com.it_num.crud_api.mapper.UserMapper;
import com.it_num.crud_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user after validating email and username uniqueness.
     *
     * @throws IllegalArgumentException if email or username is already taken
     */
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken: " + request.username());
        }

        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    /**
     * Retrieves a user by their public UUID.
     *
     * @throws ResourceNotFoundException if no user matches the given publicId
     */
    @Transactional(readOnly = true)
    public UserResponse findByPublicId(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("User", publicId));
        return userMapper.toResponse(user);
    }

    /**
     * Retrieves all users.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Updates an existing user identified by publicId.
     *
     * @throws ResourceNotFoundException if no user matches the given publicId
     * @throws IllegalArgumentException  if the new email or username is taken by another user
     */
    public UserResponse update(UUID publicId, UserUpdateRequest request) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("User", publicId));

        // Check uniqueness only if email/username has changed
        if (!user.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use: " + request.email());
        }
        if (!user.getUsername().equals(request.username())
                && userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken: " + request.username());
        }

        userMapper.updateEntity(user, request);
        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    /**
     * Deletes a user by their public UUID.
     *
     * @throws ResourceNotFoundException if no user matches the given publicId
     */
    public void delete(UUID publicId) {
        User user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException("User", publicId));
        userRepository.delete(user);
    }
}