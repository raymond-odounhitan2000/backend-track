package com.it_num.crud_api.controller;

import com.it_num.crud_api.dto.request.create.UserCreateRequest;
import com.it_num.crud_api.dto.request.update.UserUpdateRequest;
import com.it_num.crud_api.dto.response.UserResponse;
import com.it_num.crud_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{publicId}")
    public UserResponse findById(@PathVariable UUID publicId) {
        return userService.findByPublicId(publicId);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @PutMapping("/{publicId}")
    public UserResponse update(
            @PathVariable UUID publicId,
            @Valid @RequestBody UserUpdateRequest request) {
        return userService.update(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID publicId) {
        userService.delete(publicId);
    }
}