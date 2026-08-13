package com.hamropatro.dto;

import java.util.UUID;

public record UserDto(UUID userId,
                      String email,
                      String name,
                      String password,
                      String role,
                      boolean isActive,
                      long created_at,
                      long updated_at) {
}

