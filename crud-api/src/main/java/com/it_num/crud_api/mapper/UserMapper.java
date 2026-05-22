package com.it_num.crud_api.mapper;

import com.it_num.crud_api.domain.User;
import com.it_num.crud_api.dto.request.create.UserCreateRequest;
import com.it_num.crud_api.dto.request.update.UserUpdateRequest;
import com.it_num.crud_api.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setBio(request.bio());
        user.setAvatarUrl(request.avatarUrl());
        return user;
    }

    public void updateEntity(User user, UserUpdateRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setBio(request.bio());
        user.setAvatarUrl(request.avatarUrl());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getPublicId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}