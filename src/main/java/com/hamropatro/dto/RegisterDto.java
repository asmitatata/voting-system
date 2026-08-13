package com.hamropatro.dto;

import lombok.Builder;

import java.util.UUID;


@Builder
public record RegisterDto(UUID userId,
                          String email,
                          String name,
                          String password,
                          String role,
                          boolean isActive,
                          long createdAt,
                          long updatedAt) {
}
